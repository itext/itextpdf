iText consists of several jars.

The main iText release contains:
- ```itextpdf-x.y.z.jar```: the core library
- ```itext-xtra-x.y.z.jar```: extra functionality (PDF 2!)
- ```itext-pdfa-x.y.z.jar```: PDF/A-related functionality

This project is hosted on https://github.com/itext

You can find the latest releases here:
- https://github.com/itext/itextpdf
- https://github.com/itext/xtra
- https://github.com/itext/pdfa

You can also [build iText from source][building].

In some cases, you'll need [extra jars][extrajars].
These jars are bundled in a zip file here:
http://sourceforge.net/projects/itext/files/extrajars/

For XML (and HTML) functionality, you need this jar:
- ```xmlworker-x.y.z.jar```
This project is hosted on https://github.com/itext/xmlworker

Finally, we also have a tool that can help you debug PDFs:
- ```itext-rups-x.y.z.jar```
This project is hosted on https://github.com/itext/rups

If you have an idea on how to improve iText and you want to submit code,
please read our [Contribution Guidelines][contributing].

iText is licensed as [AGPL][agpl] software.

AGPL is a free / open source software license.

This doesn't mean the software is gratis!

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
[contributing]: CONTRIBUTING.md
[extrajars]: EXTRAJARS.md
