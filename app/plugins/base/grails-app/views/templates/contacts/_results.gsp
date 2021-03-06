
  <h4>Contacts matching search query</h4>
  <g:if test="${contacts}">
    <table class="table table-sortable borderless">
      <thead>
        <tr>
          <th><g:message encodeAs="HTML" code="label.givenname" /></th>
          <th><g:message encodeAs="HTML" code="label.surname" /></th>
          <th><g:message encodeAs="HTML" code="label.email" /></th>
          <th><g:message encodeAs="HTML" code="label.organization" /></th>
          <th/>
        </tr>
      </thead>
      <tbody>
      <g:each in="${contacts}" var="contact" status="i">
        <tr>
          <td>${contact.givenName?.encodeAsHTML()}</td>
          <td>${contact.surname?.encodeAsHTML()}</td>
          <td>${contact.email?.encodeAsHTML()}</td>
          <td>${contact.organization?.displayName?.encodeAsHTML()}</td>
          <td>
            <a href="${createLink(controller:'contacts', action:'show', id: contact.id)}" target="_blank" class="btn btn-small"><g:message encodeAs="HTML" code='label.view'/></a>
            <a class="btn confirm-link-contact" data-contact='${contact.id}' data-name='${contact.givenName.encodeAsHTML()} ${contact.surname.encodeAsHTML()}' data-email='${contact.email.encodeAsHTML()}'><g:message encodeAs="HTML" code="label.add" /></a>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>

    <div class="form-actions">
      <a class="btn search-for-contact"><g:message encodeAs="HTML" code="label.refinesearch" /></a>
      <a href="${createLink(controller:'contacts', action:'create')}" class="btn" target="_blank"><g:message encodeAs="HTML" code="label.newcontact" /></a>
      <a class="btn cancel-search-for-contact"><g:message encodeAs="HTML" code="label.close" /></a>
    </div>
  </g:if>
  <g:else>
    <p class=""><g:message encodeAs="HTML" code="label.noresults"/></p>
    <div>
      <a class="btn search-for-contact"><g:message encodeAs="HTML" code="label.refinesearch" /></a>
      <a href="${createLink(controller:'contacts', action:'create')}" class="btn" target="_blank"><g:message encodeAs="HTML" code="label.newcontact" /></a>
      <a class="btn cancel-search-for-contact"><g:message encodeAs="HTML" code="label.close" /></a>
    </div>
  </g:else>
