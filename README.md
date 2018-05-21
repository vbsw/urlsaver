# URL Saver

## About
URL Saver is a program to manage URLs by keywords.
URL Saver is published at <https://github.com/vbsw/urlsaver>.

Current version is 0.4.0.

## Copyright
Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).

URL Saver is distributed under the terms of the Boost Software License, version 1.0. (See accompanying file LICENSE or copy at http://www.boost.org/LICENSE_1_0.txt)

URL Saver is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Boost Software License for more details.

## Compiling
Install [Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html), [Git](https://git-scm.com) and [Eclipse](https://www.eclipse.org).

Clone this project into your Eclipse workspace

	$ git clone https://github.com/vbsw/urlsaver.git URLSaver

Open Eclipse and create a Java Project named URLSaver (like previously created directory).
To create executable jar file, right click on project, then "export...", then export as "Runnable JAR file".

## Usage
When starting URL Saver all URL files with the extension "urls.txt" are read.
These files must be in the same directory as the jar file.

A valid URL file consists of pairs of lines. The first line is the URL, the second line
are the keywords. Keywords are separated by white space (no new line between keywords).
Example URL file:

	https://vivaldi.com
	browser internet linux windows mac vivaldi
	https://git-scm.com
	git version control system distributed revision scm
	https://www.eclipse.org
	eclipse ide java compiler debugger

## Using Git
Get the master branch and all refs of this project:

	$ git clone https://github.com/vbsw/semver.git

See local and remote branches:

	$ git branch -a

Checkout other branches than master, for example the development branch:

	$ git branch development origin/development
	$ git checkout development

## References
- <https://git-scm.com/book/en/v2/Getting-Started-Installing-Git>
- <https://www.eclipse.org>
- <http://www.oracle.com/technetwork/java/javase/downloads/index.html>
