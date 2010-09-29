package fedreg.core

import static org.apache.commons.lang.StringUtils.*

import org.apache.shiro.SecurityUtils

class DescriptorEndpointController {

	static allowedMethods = [delete: "POST", create: "POST", toggle:"POST", listEndpoints:"GET"]
	
	// Maps allowed endpoints to internal class representation
	def allowedEndpoints = [singleSignOnServices:"fedreg.core.SingleSignOnService", artifactResolutionServices:"fedreg.core.ArtifactResolutionService", 
							singleLogoutServices:"fedreg.core.SingleLogoutService", assertionConsumerServices:"fedreg.core.AssertionConsumerService", attributeServices:"fedreg.core.AttributeService", discoveryResponseServices:"fedreg.core.DiscoveryResponseService"]

	def endpointService

	def delete = {
		if(!params.id) {
			log.warn "Endpoint ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.endpointType) {
			log.warn "Endpoint type was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def endpoint = Endpoint.get(params.id)
		if(!endpoint) {
			log.warn "Endpoint identified by id $params.id was not located"
			render message(code: 'fedreg.endpoint.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		if(SecurityUtils.subject.isPermitted("descriptor:${endpoint.descriptor.id}:endpoint:remove")) {
			endpointService.delete(endpoint, params.endpointType)
			render message(code: 'fedreg.endpoint.delete.success')
		}
		else {
			log.warn("Attempt to remove $endpoint by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def list = {
		if(!params.id) {
			log.warn "Descriptor ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.endpointType) {
			log.warn "Endpoint type was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.containerID) {
			log.warn "Container ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def endpointType = params.endpointType
		def descriptor = RoleDescriptor.get(params.id)
		if (!descriptor) {
			log.warn "Descriptor was not found for id ${params.id}"
			render message(code: 'fedreg.roledescriptor.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		// Determine if we're actually listing from the collaborator (useful for AA endpoints on IDP screen)
		if(!descriptor.hasProperty(endpointType)) {
			if(descriptor.collaborator.hasProperty(endpointType))
				descriptor = descriptor.collaborator
		}
		
		if(allowedEndpoints.containsKey(endpointType) && descriptor.hasProperty(endpointType)) {
			log.debug "Listing endpoints for descriptor ID ${params.id} of type ${endpointType}"
			render template:"/templates/endpoints/list", contextPath: pluginContextPath, model:[endpoints:descriptor."${endpointType}", allowremove:true, endpointType:endpointType, containerID:params.containerID]
		}
		else {
			log.warn "Endpoint ${endpointType} is invalid for Descriptor with id ${params.id}"
			render message(code: 'fedreg.endpoint.invalid', args: [endpointType])
			response.setStatus(500)
			return
		}
	}
	
	def create = {
		if(!params.id) {
			log.warn "Descriptor ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.endpointType) {
			log.warn "Endpoint type was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.binding) {
			log.warn "Binding ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.location) {
			log.warn "Location URI was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def endpointType = params.endpointType
		def descriptor = RoleDescriptor.get(params.id)
		if (!descriptor) {
			log.warn "Descriptor was not found for id ${params.id}"
			render message(code: 'fedreg.roledescriptor.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		if(SecurityUtils.subject.isPermitted("descriptor:${descriptor.id}:endpoint:create")) {
			// Determine if we're actually updating the collaborator (useful for AA endpoints on IDP screen)
			if(!descriptor.hasProperty(endpointType)) {
				if(descriptor.collaborator.hasProperty(endpointType))
					descriptor = descriptor.collaborator
			}
		
			def binding = SamlURI.get(params.binding)
			if (!binding) {
				log.warn "SamURI (binding) was not found for id ${params.id}"
				render message(code: 'fedreg.samluri.nonexistant', args: [params.binding])
				response.setStatus(500)
				return
			}
		
			if(allowedEndpoints.containsKey(endpointType) && descriptor.hasProperty(endpointType)) {
				endpointService.create(descriptor, allowedEndpoints.get(endpointType), endpointType, binding, params.location)
				render message(code: 'fedreg.endpoint.create.success')
			}
			else {
				log.warn "Endpoint ${endpointType} is invalid for ${descriptor}, unable to create"
				render message(code: 'fedreg.endpoint.invalid', args: [endpointType])
				response.setStatus(500)
			}
		}
		else {
			log.warn("Attempt to create endpoint for $descriptor by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def toggle = {
		if(!params.id) {
			log.warn "Endpoint ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(500)
			return
		}
		
		def endpoint = Endpoint.get(params.id)
		if(!endpoint) {
			log.warn "Endpoint identified by id $params.id was not located"
			render message(code: 'fedreg.endpoint.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
	
		if(SecurityUtils.subject.isPermitted("descriptor:${endpoint.descriptor.id}:endpoint:toggle")) {
			endpointService.toggle(endpoint)
			render message(code: 'fedreg.endpoint.toggle.success')
		}
		else {
			log.warn("Attempt to toggle $endpoint state by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
}