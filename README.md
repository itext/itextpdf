### **PLEASE NOTE: iText 5 is EOL, and has been replaced by [iText][itext].  Only security fixes will be added**

### Known Security Issues

#### com.itextpdf:itextpdf vulnerabilities
There are 2 CVEs that are often attributed to iText 5: [CVE-2022-24196](https://nvd.nist.gov/vuln/detail/CVE-2022-24196)
and [CVE-2022-24197](https://nvd.nist.gov/vuln/detail/CVE-2022-24197). Both are false positives and do not apply
to iText 5; they are reported due to an incorrect artifact regex in some CVE databases.

#### org.apache.santuario:xmlsec vulnerabilities
The iText 5 targets Java 5 which means that we can not update `org.apache.santuario:xmlsec` version to 2.x.x or newer as
it requires Java 8. If you are not using the com.itextpdf.text.pdf.security.MakeXmlSignature class then you can avoid
adding `org.apache.santuario:xmlsec` dependency into your project. Which means that you would not be affected by
the related vulnerabilities, for example https://snyk.io/vuln/SNYK-JAVA-ORGAPACHESANTUARIO-1655558. If you are using
com.itextpdf.text.pdf.security.MakeXmlSignature class, for example for XFA signatures, then you can:
- either use `org.apache.santuario:xmlsec` 1.5.8 as a dependency which is affected by the vulnerability specified above,
 but works on Java 5+;
- or use `org.apache.santuario:xmlsec` 2.1.7 or newer. But this would require java 8+ and affects on the output format
(see https://issues.apache.org/jira/browse/SANTUARIO-494).

#### commons-io:commons-io vulnerabilities
The Java 5-compatible modules in iText 5 use a `commons-io:commons-io` version that is vulnerable (for example,
[CVE-2021-29425](https://nvd.nist.gov/vuln/detail/CVE-2021-29425)). Because these modules target Java 5, they cannot
upgrade `commons-io:commons-io` to 2.7 or newer (which requires Java 8).

If you are using Java 8 or higher, specify direct dependency to the latest `commons-io:commons-io` version to 
mitigate CVEs, used version in iText is compatible with latest releases of `commons-io`.

We **HIGHLY** recommend customers use iText 9 for new projects, and to consider moving existing projects from iText 5 to iText 9 to benefit from the many improvements such as:
 
- HTML to PDF (PDF/UA) conversion
- Wider PDF standards support f.e. PDF/A-4, PDF/UA-2, WTPDF etc.
- PDF Redaction
- SVG support
- Better language support: Indic, Thai, Khmer, Arabic, Hebrew. (Close-source addon)
- PDF Debugging for your IDE
- Data Extraction
- Better continued support and bugfixes
- More modular, extensible handling of your document workflow
- Extra practical add-ons
- Encryption, hashing & digital signatures


### [iText 5][itext] consists of several jars.

The main release contains:
- ```itextpdf-x.y.z.jar```: the core library
- ```itext-xtra-x.y.z.jar```: extra functionality (PDF 2!)
- ```itext-pdfa-x.y.z.jar```: PDF/A-related functionality
- ```xmlworker-x.y.z.jar```: XML (and HTML) functionality

iText 5 is hosted on https://github.com/itext/itextpdf

You can find the latest releases here:
- http://github.com/itext/itextpdf/releases/latest

You can also [build iText 5 from source][building].

We also have RUPS — a tool that can help you debug PDFs. It's hosted on http://github.com/itext/rups

iText is licensed as [AGPL][agpl] software.

AGPL is a free / open source software license.

This doesn't mean the software is [gratis][gratis]!

Buying a license is mandatory as soon as you develop commercial activities
distributing the iText software inside your product or deploying it on a network
without disclosing the source code of your own applications under the AGPL license.
These activities include:
- offering paid services to customers as an ASP
- serving PDFs on the fly in the cloud or in a web application
- shipping iText with a closed source product

Contact sales for more info: http://itextpdf.com/sales

[agpl]: LICENSE.md
[building]: BUILDING.md
[extrajars]: EXTRAJARS.md
[gratis]: https://en.wikipedia.org/wiki/Gratis_versus_libre
[itext]: http://itextpdf.com/
[itext]: https://github.com/itext/itext
