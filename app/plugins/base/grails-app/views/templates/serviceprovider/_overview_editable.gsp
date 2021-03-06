<div id="overview-serviceprovider-editable">
  <table class="table borderless fixed">
    <tbody>  
      <tr>
        <th><g:message encodeAs="HTML" code="label.status"/></th>
        <td>
          <g:if test="${serviceProvider.active}">
            <g:message encodeAs="HTML" code="label.active" />
          </g:if>
          <g:else>
            <span class="label label-important"><g:message encodeAs="HTML" code="label.inactive" /></span><fr:tooltip code='label.warningmetadata'/>
          </g:else>
        </td>
      </tr> 
      <tr>
        <th><g:message encodeAs="HTML" code="label.displayname"/></th>
        <td>${fieldValue(bean: serviceProvider, field: "displayName")}</td>
      </tr>
      <tr>
        <th>
          <g:message encodeAs="HTML" code="label.description" />
        </th>
        <td>
          ${fieldValue(bean: serviceProvider, field: "description")}
        </td>
      </tr>
      <tr>
        <th>
          <g:message encodeAs="HTML" code="label.serviceurl" />
        </th>
        <td>
          ${fieldValue(bean: serviceProvider, field: "serviceDescription.connectURL")}
        </td>
      </tr>
      <tr>
        <th>
          <g:message encodeAs="HTML" code="label.servicelogourl" />
        </th>
        <td>
          ${fieldValue(bean: serviceProvider, field: "serviceDescription.logoURL")}
        </td>
      </tr>
    </tbody>
  </table>
</div>
