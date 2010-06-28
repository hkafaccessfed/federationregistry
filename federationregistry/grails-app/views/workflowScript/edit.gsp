
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="workflow" />
		<title><g:message code="fedreg.view.workflow.script.edit.title" /></title>
		
		<script src="${request.contextPath}/js/codemirror/js/codemirror.js" type="text/javascript" charset="utf-8"></script>
	</head>
	<body>
		<h2><g:message code="fedreg.view.workflow.script.edit.heading" args="[script.name]"/></h2>
		
		<g:if test="${script.hasErrors()}">
			<div class="error">
			<ul>
			<g:eachError bean="${script}">
			    <li>${it}</li>
			</g:eachError>
			</ul>
			</div>
		</g:if>
		
		<g:form action="update" id="${script.id}">			
			<table>
				<tbody>
					<tr>
						<td><label for="name"><g:message code="fedreg.label.name" /></label></td>
						<td><g:textField name="name" value="${script.name ?: ''}" /></td>
					</tr>
					<tr>
						<td><label for="description"><g:message code="fedreg.label.description" /></label></td>
						<td><g:textField name="description" value="${script.description ?: ''}" /></td>
					</tr>
					<tr>
						<td><label for="definition"><g:message code="fedreg.label.definition" /></label></td>
						<td><g:textArea name="definition" value="${(script.definition ?: '// Script definition').encodeAsHTML()}" rows="5" cols="40"/></td>
					</tr>
				</tbody>
			</table>
			<button type="submit" class="button icon icon_accept"/><g:message code="fedreg.link.update" /></button>
		</g:form>
		
		<script type="text/javascript">
			 var textarea = $("#definition");
			  var editor = CodeMirror.fromTextArea('definition',  {
	            height: "300px",
	            content: textarea.value,
	            parserfile: ["tokenizegroovy.js", "parsegroovy.js"],
	            stylesheet: "${request.contextPath}/js/codemirror/css/groovycolors.css",
	            path: "${request.contextPath}/js/codemirror/js/",
	            autoMatchParens: true,
	            disableSpellcheck: true,
	            lineNumbers: true,
	            tabMode: 'shift'
	         });
		</script>
		
	</body>
</html>