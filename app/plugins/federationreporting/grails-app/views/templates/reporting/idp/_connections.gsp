
<div class="description">
	<h4 id="connectivitytitle"></h4>
	<p><g:message code="fedreg.templates.reports.identityprovider.connectivity.description"/></p>

	<div class="reportrefinement">
		<form id='connectivityrefinement' class="reportrefinementinput loadhide">
			<h5><g:message code="fedreg.templates.reports.identityprovider.connectivity.refinement.title"/> ( <a href="#" onClick="$('#connectivitycomponents :unchecked').attr('checked', true); return false;"><g:message code="label.addallchecks" /></a> | <a href="#" onClick="$('#connectivitycomponents :checked').attr('checked', false); return false;"><g:message code="label.removeallchecks" /></a> )</h5>
			<input type="hidden" name='activesp' value='0'/>
			
			
			
			<span id="connectivitycomponents" class="reportrefinementcomponenets">
			</span>
			
			<div  class="buttons">
				<a href="#" onClick="fedreg.refineIdPConnectivityReport($('#connectivityrefinement')); return false;" class="update-button"><g:message code="label.update" /></a>
				<a href="#" onClick="$('.reportrefinementinput').slideUp(); $('.reportrefinementopen').show(); return false;" class="close-button"><g:message code="label.close" /></a>
			</div>
		</form>
		<div class="reportrefinementopen">
			<a href="#" onClick="$('.reportrefinementopen').hide(); $('.reportrefinementinput').slideDown(); return false;" class="download-button"><g:message code="label.refine" /></a>
		</div>
	</div>
</div>

<div id="connectivitydata">
</div>

<script type="text/javascript+protovis">
	renderConnectivity = function(data, refine) {
		$('#connectivitydata').empty();
		$('#connectivitytitle').html(data.title);
		
		if(!refine) {
			$('#connectivitycomponents').empty();
			$.each( data.services, function(index, sp) {
				if(sp.rendered)
					$("#connectivitycomponents").append("<label class='choice'><input type='checkbox' checked='checked' name='activesp' value='"+sp.id+"'></input>"+sp.name+"</label>");
				else
					$("#connectivitycomponents").append("<label class='choice'><input type='checkbox' name='activesp' value='"+sp.id+"'></input>"+sp.name+"</label>");
			});
		}
		
		var canvas = document.createElement("div");
		$('#connectivitydata').append(canvas);
		var vis = new pv.Panel()
			.canvas(canvas)
			.width(900)
			.height(500)
			.bottom(220);

		var arc = vis.add(pv.Layout.Arc)
			.reset()
		    .nodes(data.nodes)
		    .links(data.links);
	
		var line = arc.link.add(pv.Line)
		

		var dot = arc.node.add(pv.Dot)
		    .size(function(d) d.linkDegree);

		arc.label.add(pv.Label)
		vis.render();
	};
</script>