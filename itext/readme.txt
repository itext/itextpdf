To build iText, maven must be installed. ( http://maven.apache.org/ )
Running install without a profile will generate the itextpdf jar. (mvn install)
When using the profile 'all' also the source and javadoc jars will be generated.( mvn install -P all )
If you are in need of the asian font jars. You can run one of the following commands:
    mvn clean install -f itext-asian.pom
    mvn clean install -f itext-asiancmaps.pom
If you need the hyphenation jar, execute:
    mvn clean install -f itext-hyph-xml.pom
