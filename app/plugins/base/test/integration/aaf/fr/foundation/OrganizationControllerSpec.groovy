package aaf.fr.foundation

import org.codehaus.groovy.grails.web.servlet.mvc.SynchronizerTokensHolder

import grails.plugin.spock.*
import aaf.fr.workflow.*
import aaf.fr.identity.Subject

class OrganizationControllerSpec extends IntegrationSpec {

  def controller, organizationService, user

  def cleanup() {
    user.permissions = []
  }

  def setup () {
    organizationService = new OrganizationService()   
    controller = new OrganizationController(organizationService:organizationService)

    user = Subject.build()
    SpecHelpers.setupShiroEnv(user)
  }

  def "Validate list"() {
    setup:    
    (1..25).each { i ->
     def org = Organization.build()
     org.save()
   }

   when:
   def model = controller.list()

   then:
   model.organizationList.size() >= 25
 }

 def "Show with no ID"() {   
  when:
  controller.show()

  then:
  controller.flash.type == "error"
  controller.flash.message == "controllers.fr.generic.namevalue.missing"
  controller.response.redirectedUrl == "/membership/organization/list"
}

def "Show with invalid Organization ID"() {
  setup:
  controller.params.id = 20000000

  when:
  controller.show()

  then:
  controller.flash.type == "error"
  controller.flash.message == "domains.fr.foundation.organization.nonexistant"
  controller.response.redirectedUrl == "/membership/organization/list"
}

def "Validate create"() {
  setup:
  (1..10).each {
   OrganizationType.build().save()
 }

 when:
 def model = controller.create()

 then:
 model.organization != null
 model.organization instanceof Organization
}

def "Validate successful save"() {
  setup:
  def organization = Organization.build().save()

  def token = SynchronizerTokensHolder.store(controller.session)
  controller.params[SynchronizerTokensHolder.TOKEN_URI] = "/organization/save"
  controller.params[SynchronizerTokensHolder.TOKEN_KEY] = token.generateToken(controller.params[SynchronizerTokensHolder.TOKEN_URI])

  when:
  organizationService.metaClass.create = { def p -> 
   return [true, organization]
 } 
 def model = controller.save()

 then:
 controller.response.redirectedUrl == "/membership/organization/show/${organization.id}"  
}

def "Validate failed save"() {
  setup:
  def organization = Organization.build().save()

  def token = SynchronizerTokensHolder.store(controller.session)
  controller.params[SynchronizerTokensHolder.TOKEN_URI] = "/organization/save"
  controller.params[SynchronizerTokensHolder.TOKEN_KEY] = token.generateToken(controller.params[SynchronizerTokensHolder.TOKEN_URI])

  organizationService.metaClass.create = { def p -> 
    return [false, organization]
  } 

  when:
  def model = controller.save()

  then:
  controller.flash.type == "error"
  controller.flash.message == "domains.fr.foundation.organization.save.validation.error"  
}

def "Validate successful update"() {
  setup:
  def organization = Organization.build().save()

  controller.params.id = organization.id
  user.permissions.add("federation:management:organization:${organization.id}:update")
  organizationService.metaClass.update = { def p -> 
    return [true, organization]
  } 

  when:
  controller.update()

  then:
  controller.response.redirectedUrl == "/membership/organization/show/${organization.id}" 
}

def "Validate update with incorrect perms"() {
  setup:
  def organization = Organization.build().save()

  controller.params.id = organization.id
  user.permissions.add("organization:-1:update")

  when:
  controller.update()

  then:
  controller.response.status == 403   
}

def "Invalid or non existing Organization fails update"() {
  setup:    
  controller.params.id = 2000000

  when:
  def model = controller.update()

  then:
  controller.response.redirectedUrl == "/membership/organization/list"  
  controller.flash.type == "error"
  controller.flash.message == "domains.fr.foundation.organization.nonexistant"
}

def "Invalid organization data fails update"() {
  setup:
  def organization = Organization.build().save()

  controller.params.id = organization.id
  user.permissions.add("federation:management:organization:${organization.id}:update")

  when:
  organizationService.metaClass.update = { def p -> 
   return [false, organization]
 } 
 def model = controller.update()

 then:    
 controller.flash.type == "error"
 controller.flash.message == "domains.fr.foundation.organization.update.validation.error" 
}
}
