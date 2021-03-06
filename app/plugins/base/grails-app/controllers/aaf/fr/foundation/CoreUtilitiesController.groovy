package aaf.fr.foundation

import grails.converters.JSON

/**
 * Provides core utility views.
 *
 * @author Bradley Beddoes
 */
class CoreUtilitiesController {
	def allowedMethods = [validateCertificate: 'POST']

	def grailsApplication
	def cryptoService

	def knownIDPImpl = {
		render grailsApplication.config.aaf.fr.knownimplementations.identityproviders as JSON
	}

	def knownSPImpl = {
		render grailsApplication.config.aaf.fr.knownimplementations.serviceproviders as JSON
	}

	def validateCertificate = {
		if(!params.cert || params.cert.length() == 0) {
			log.warn "Certificate data was not present"
			render template:"/templates/certificates/validation", contextPath: pluginContextPath, model:[corrupt:true]
			response.setStatus(500)
			return
		}

		log.debug "About to validate new certificate:\n${params.cert}"
    def data = params.cert.trim()
		try {
      if(!(data.startsWith('-----BEGIN CERTIFICATE-----')) || !(data.endsWith('-----END CERTIFICATE-----'))){
        render template:"/templates/certificates/validation", contextPath: pluginContextPath, model:[corrupt:true, certerrors:["templates.fr.certificates.validation.banners"]]
        response.setStatus(500)
        return
      }

      if(data.count('-----BEGIN CERTIFICATE-----') > 1 || data.count('-----END CERTIFICATE-----') > 1 ){
        render template:"/templates/certificates/validation", contextPath: pluginContextPath, model:[corrupt:true, certerrors:["templates.fr.certificates.validation.chain"]]
        response.setStatus(500)
        return
      }

			def certificate = cryptoService.createCertificate(data.normalize())
			def subject = cryptoService.subject(certificate);
			def issuer = cryptoService.issuer(certificate);
			def expires = cryptoService.expiryDate(certificate);

			def valid = true
			def certerrors = []

			log.info "Attempting to validate certificate data:\n$certificate"

			// Max validity matches configured allowable value
			def today = new Date()
			def maxValidDate = today + grailsApplication.config.aaf.fr.certificates.maxlifeindays
			if(expires.after(maxValidDate)) {
				valid = false
				certerrors.add("templates.fr.certificates.validation.expiry.tolong")
				log.warn "Certificate exceeds max time period for validity"
			}

			render template:"/templates/certificates/validation",  contextPath: pluginContextPath, model:[corrupt: false, subject:subject, issuer:issuer, expires:expires, valid:valid, certerrors:certerrors]
			if(!valid) {
				log.warn "Certificate being marked as invalid, progression will be halted"
				response.setStatus(500)
			}
		}
		catch(Exception e) {
			log.debug e
			log.warn "Certificate data is invalid"
			render template:"/templates/certificates/validation", contextPath: pluginContextPath, model:[corrupt:true]
			response.setStatus(500)
		}
	}
}
