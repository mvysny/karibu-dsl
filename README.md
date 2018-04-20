# Karibu-DSL (Vaadin Kotlin Extensions)

[![Build Status](https://travis-ci.org/mvysny/karibu-dsl.svg?branch=master)](https://travis-ci.org/mvysny/karibu-dsl)
[![Join the chat at https://gitter.im/vaadin/vaadin-on-kotlin](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/vaadin/vaadin-on-kotlin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![GitHub tag](https://img.shields.io/github/tag/mvysny/karibu-dsl.svg)](https://github.com/mvysny/karibu-dsl/tags)
[![Heroku](https://heroku-badge.herokuapp.com/?app=karibu-uitest&style=flat&svg=1)](https://karibu-uitest.herokuapp.com/)

This is a Kotlin extensions and DSL library for the [Vaadin](https://www.vaadin.com) framework.
Please visit [Vaadin-on-Kotlin](http://www.vaadinonkotlin.eu/) pages for the Getting Started guide.

This library:

* Allows you to create Vaadin UI designs/component graphs in the structured code way; the idea behind
  is explained in the [Writing Vaadin apps in Kotlin Part 4](http://mavi.logdown.com/posts/1493730) article.
  The general DSL idea is explained in [Kotlin Type-Safe Builders](https://kotlinlang.org/docs/reference/type-safe-builders.html).
* Auto-discovery and auto-mapping of Views via a custom Navigator
* Additional useful methods which Vaadin lacks

Supports:

* [Vaadin 8](https://vaadin.com/framework) (including support for v7 compatibility package)
* [Vaadin 10 aka Flow](https://vaadin.com/flow)

The origins of the word *Karibu*: it's a term for North American subspecies of the reindeer; that connects to
*Vaadin* (which is a Finnish word for a female reindeer). A nice connotation comes from Swahili where *Karibu*
means *welcome*. 

## Why DSL?

Just compare the Kotlin-based [CommonElementsView](example-v8/src/main/kotlin/com/github/vok/karibudsl/example/CommonElementsView.kt)
with the original Java [CommonParts](https://github.com/vaadin/framework/blob/master/uitest/src/main/java/com/vaadin/tests/themes/valo/CommonParts.java).
Both render the [Common UI Elements](https://karibu-uitest.herokuapp.com/common-elements) page, yet with Kotlin DSL:

* The UI structure is immediately visible
* The code is more readable and much shorter and concise
* You can more easily copy parts of the UI and paste it into your project

## Further Steps

The documentation differs for Vaadin 8 and for Vaadin 10:

* [Vaadin 8 tutorial](karibu-dsl-v8)
* [Vaadin 10 tutorial](karibu-dsl-v10)
