# WebsiteCrawler
This is a website crawler based on DOM Parsing in Java. You may configure it in your eclipse as well.

Thanks to Jodd for the amazing set of Java micro frameworks, tools and utilities. (http://jodd.org/)

<b>Prerequisites : <br /></b>
1) Minimum Java 8 is required to run this. Please download latest version of Java from <a href="https://www.java.com/">here</a>

<b>Features :<br /></b>
1) Crawl your URL using command line. Multiple command line arguments are available to use. <br />
2) Automatically filters external links and popular file formats like jpg, png, gif, js and pdf. <br />
3) Either display the crawling results in console or save it to HTML file for future reference. <br />

<b>Usage : <br/></b>
1) Configure the new project in Eclipse. You will need to add the dependency of "lib/jodd-all-3.8.0.jar" file in your build path. <br />
2) Compile and run the "WebsiteURLCrawler.java/class" file with appropriate command line arguments.<br />
3) Currently it will filter following type of data : External URL, PDF, JPG/JPEG, PNG and GIF. <br />
4) Sample jar file from the latest build and a HTML file that contains crawling results from popular online coding website <a href="https://www.hackerrank.com">hackerrank</a> is added in repo as WebsiteCrawler/sample/WebsiteCrawler.jar. Hit "<b>java -jar WebsiteCrawler.jar -help</b>" to see all available options. <br />
5) Should you need any help in using/configuring or you have any suggestions or improvements or want to contribute, please shoot me an email (avixit.aparnathi@gmail.com) with all the details. I shall definitely get back to you. <br />

<b>Future Enhancements : <br /></b>
1) Add cookie support to fetch all the url's after login in a website. <br />
2) Add support to save the crawling result in multiple file formats (i.e text, XLS, PDF etc). <br />
3) Add dynamic filtering (i.e If you want only images or PDF or any specific type of files from a website). <br />
4) Add E-mail Id and mobile number filters. <br />

Happy Crawling !