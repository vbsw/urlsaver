# URL Saver

## About
URL Saver is a program to manage URLs by tags (i.e. keywords).
URL Saver is published at <https://github.com/vbsw/urlsaver>.

Current version is 0.1.0.

## Copyright
Copyright 2016, Vitali Baumtrok (vbsw@mailbox.org).

URL Saver is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

URL Saver is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with URL Saver.  If not, see <http://www.gnu.org/licenses/>.

## Compiling
Download Eclipse: <https://www.eclipse.org>

Clone the repository into your Eclipse workspace

	$ git clone https://github.com/vbsw/urlsaver.git URLSaver

Open Eclipse and create a Java Project named URLSaver (like previously created directory).
To create executable jar file, right click on project, then "export...", then export as "Runnable JAR file".

## URL File Example
Two consecutive lines with at least one character other then white space are
a URL (first line) and tags (second line). Tags are separated by white space
(no new line between tags). Example:

	http://www.dict.cc
	dictionary language lang dict
	https://git-scm.com
	git version control system distributed revision
	https://www.eclipse.org
	eclipse ide java compiler debugger

## References
- <https://git-scm.com/book/en/v2/Getting-Started-Installing-Git>
- <https://www.eclipse.org>
- <http://www.oracle.com/technetwork/java/javase/downloads/index.html>
