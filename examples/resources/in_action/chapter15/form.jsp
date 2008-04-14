<%@ page import="java.io.*" %><% 
	BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
	response.setContentType("text/plain");
	String line;
	while ((line = reader.readLine()) != null) {
		writer.write(line);
		writer.write("\n");
	} 
	reader.close();
	writer.close();
%>