let findB = false;
function init()
{
	show("dashDiv");
}

function show(v)
{
	hideAll();
	let n = document.getElementById(v);
	n.style.display = "block";
}

function hideAll()
{
	let list = document.getElementsByClassName("view");
	for (let e of list)
	{
		e.style.display = "none";
	}
}

function sis(f)
{
	let prefix = f.elements["prefix"].value;
	let minGpa = f.elements["minGpa"].value;
	let sortBy = f.elements["sortBy"].value;
	let calc = f.elements["calc"].value;
	let qs = "";

	 
 	qs = "calc=" + calc + "&minGpa=" + minGpa + "&sortBy=" + sortBy + "&prefix=" + prefix;
	
	doSimpleAjax("Sis.do", qs, sisResp);
}

function prime(f)
{
	let min = f.elements["min"].value;
	let max = f.elements["max"].value;
	let calc = f.elements["calc"].value;
	let pre = f.elements["previous"].value;
	let qs = "";

	if (findB) 
 	 qs = "calc=" + calc + "&min=" + min + "&max=" + max + "&previous=";
	else 
		qs = "calc=" + calc+ "&min=" + min + "&max=" + max + "&previous="+pre;
	
	findB = false;
	
		
	doSimpleAjax("Prime.do", qs, primeResp);
}

function find() {
	findB = true;
}


function primeResp(request)
{
	if ((request.readyState == 4) && (request.status == 200)) 
	{
		let resp = JSON.parse(request.responseText);
		let html = "";
		if (resp.status == 1)
		{
			
			html += "<input type='hidden' id='previous' name='previous' value='" + resp.result + "'/>";
			html +=  "<div class='form-group'><input value='Next' id='calcNext' class='btn btn-lg btn-primary btn-block' type='submit'/></div>";
			html += "<p>The answer is " + resp.result + "<p>";
			//document.getElementById("previous").value = resp.result;
		}
		else
		{
			html += "<p style='color:red'>Error: " + resp.error + "</p>";
		}
		document.getElementById("addResult").innerHTML = html ;
	}
}

function sisResp(request)
{
	if ((request.readyState == 4) && (request.status == 200)) 
	{
		let resp = JSON.parse(request.responseText);
		let html = "";
		if (resp.status == 1)
		{
			
			html += "<div>" + resp.result + "</div>";
		}
		else
		{
			html += "<div style='color:red'>Error: " + resp.error + "</div>";
		}
		document.getElementById("sisResult").innerHTML = html ;
	}
}

function doSimpleAjax(address, data, handler)
{
    var request = new XMLHttpRequest();
    request.onreadystatechange = function() {handler(request);};
    request.open("post", address+"?"+data, true);
    request.send(null);
}




