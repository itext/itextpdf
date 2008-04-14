<%@ page import="java.io.*,java.util.HashMap,com.lowagie.text.pdf.*" %><% 
	FdfReader reader = new FdfReader(request.getInputStream());
%><html>
<head><title>Learning Agreement</title></head>
<body>
<h2>Learning Agreement</h2>
<table>
<tr><td>Academic year</td><td><%= reader.getFieldValue("academic_year") %></td></tr>
<tr><td>Student name</td><td><%= reader.getFieldValue("student_name") %></td></tr>
<tr><td>Sending Institution</td><td><%= reader.getFieldValue("sending_institution") %> (<%= reader.getFieldValue("sending_country") %>)</td></tr>
<tr><td>Receiving Institution</td><td><%= reader.getFieldValue("receiving_institution") %> (<%= reader.getFieldValue("receiving_country") %>)</td></tr>
<tr><td valign="Top">Courses:</td>
<td>
<table>
<%
String parent;
for (int i = 0; i < 16; i++) {
	parent = "course_" + i + ".";
	if (reader.getFieldValue(parent + "code") != null) {
%>
<tr><td><%= reader.getFieldValue(parent + "code") %></td><td><%= reader.getFieldValue(parent + "name") %></td><td><%= reader.getFieldValue(parent + "credits") %></td></tr>
<%
	}
}
%>
</table>
</td>

</table>
</body>
</html><%
	reader.close();
%>