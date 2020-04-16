[![Powered By Vaadin on Kotlin](http://vaadinonkotlin.eu/iconography/vok_badge.svg)](http://vaadinonkotlin.eu)
[![Build Status](https://travis-ci.org/mvysny/karibu-dsl.svg?branch=master)](https://travis-ci.org/mvysny/karibu-dsl)
[![Join the chat at https://gitter.im/vaadin/vaadin-on-kotlin](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/vaadin/vaadin-on-kotlin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![GitHub tag](https://img.shields.io/github/tag/mvysny/karibu-dsl.svg)](https://github.com/mvysny/karibu-dsl/tags)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mvysny.karibudsl/karibu-dsl-v8/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mvysny.karibudsl/karibu-dsl-v8)
[![Heroku](https://heroku-badge.herokuapp.com/?app=karibu-uitest&style=flat&svg=1)](https://karibu-uitest.herokuapp.com/)

# Karibu-DSL (Vaadin Kotlin Extensions)

This is a Kotlin extensions and DSL library for the [Vaadin](https://www.vaadin.com) framework.
Please visit [Vaadin-on-Kotlin](http://www.vaadinonkotlin.eu/) pages for the Getting Started guide.

This library:

* Allows you to create Vaadin UI designs in the structured code way; the idea behind the library
  is explained in the [DSLs: Explained for Vaadin 10](https://www.vaadinonkotlin.eu/dsl_explained-v10.html) article
  (or for older Vaadin 8: [DSLs: Explained for Vaadin 8](https://www.vaadinonkotlin.eu/dsl_explained.html)).
  The general DSL idea is explained in [Kotlin Type-Safe Builders](https://kotlinlang.org/docs/reference/type-safe-builders.html).
* (Vaadin 8 only): Auto-discovery and auto-mapping of Views via a custom Navigator
* Additional useful methods which Vaadin lacks

The documentation differs for Vaadin 14 and for older Vaadin 8:

* [Karibu-DSL Vaadin 13, 14, 15+ tutorial](karibu-dsl)
* [Karibu-DSL Vaadin 8 tutorial](karibu-dsl-v8)

The origins of the word *Karibu*: it's a term for North American subspecies of the reindeer; that connects to
*Vaadin* (which is a Finnish word for a female reindeer). A nice connotation comes from Swahili where *Karibu*
means *welcome*. 

> Note: For Groovy Vaadin DSL please see the [Vaadin Groovy Builder](https://github.com/mvysny/vaadin-groovy-builder) library.

## Why DSL?

Just compare the Kotlin-based [CommonElementsView](example-v8/src/main/kotlin/com/example/v8/uitest/example/CommonElementsView.kt)
with the original Java [CommonParts](https://github.com/vaadin/framework/blob/master/uitest/src/main/java/com/vaadin/tests/themes/valo/CommonParts.java).
Both render the [Common UI Elements](https://karibu-uitest.herokuapp.com/common-elements) page, yet with Kotlin DSL:

* The UI structure is immediately visible
* The code is more readable and much shorter and concise
* You can more easily copy parts of the UI and paste it into your project

Supports:

* [Vaadin 13/14/15+](https://vaadin.com/)
* Older [Vaadin 8](https://vaadin.com/vaadin-8) (including support for v7 compatibility package)

## Upgrading

The Maven dependency `groupId` has been changed in order for the library to be eligible for Maven Central. Also, all files have been moved to a proper package.
To upgrade to 0.5.x from 0.4.x:

* Change the `groupId` from `com.github.vok.karibudsl` to `com.github.mvysny.karibudsl`
* In all of your Vaadin 8 project files, search for a string `import com.github.vok.karibudsl.` and replace it with `import com.github.mvysny.karibudsl.v8.`. You can easily achieve this with
  Intellij, by launching _Replace In Path_ via `Ctrl+Shift+R` and selecting the Scope of _Whole Project_. For Vaadin 10-based project, search for `import com.github.vok.karibudsl.flow.`
  and replace that with `import com.github.mvysny.karibudsl.v10.` instead.

To upgrade to 1.0.0 from 0.x.y:
* Change the `karibu-dsl-v10` dependency to just `karibu-dsl`.

# License

Licensed under the [MIT License](https://opensource.org/licenses/MIT).

Copyright (c) 2017-2020 Martin Vysny

All rights reserved.

Permission is hereby granted, free  of charge, to any person obtaining
a  copy  of this  software  and  associated  documentation files  (the
"Software"), to  deal in  the Software without  restriction, including
without limitation  the rights to  use, copy, modify,  merge, publish,
distribute,  sublicense, and/or sell  copies of  the Software,  and to
permit persons to whom the Software  is furnished to do so, subject to
the following conditions:

The  above  copyright  notice  and  this permission  notice  shall  be
included in all copies or substantial portions of the Software.
THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
