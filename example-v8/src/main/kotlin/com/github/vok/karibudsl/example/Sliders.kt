/*
 * Copyright 2000-2016 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.vok.karibudsl.example

import com.github.vok.karibudsl.*
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.shared.ui.slider.SliderOrientation
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme
import org.slf4j.LoggerFactory

@AutoView
class Sliders : VerticalLayout(), View {
    private lateinit var pb: ProgressBar
    private lateinit var pb2: ProgressBar
    init {
        isSpacing = false
        title("Sliders")

        horizontalLayout {
            addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING)
            slider("Horizontal") {
                value = 50.0
            }
            slider("Horizontal, sized") {
                value = 50.0
                w = 200.px
            }
            slider("Custom handle") {
                value = 50.0
                w = 200.px
                addStyleName("color1")
            }
            slider("Custom track") {
                value = 50.0
                w = 200.px
                addStyleName("color2")
            }
            slider("Custom indicator") {
                value = 50.0
                w = 200.px
                addStyleName("color3")
            }
            slider("No indicator") {
                value = 50.0
                w = 200.px
                addStyleName(ValoTheme.SLIDER_NO_INDICATOR)
            }
            slider("With ticks") {
                value = 3.0
                w = 200.px
                max = 4.0
                addStyleName("ticks")
            }
            slider("Toggle imitation") {
                w = 50.px
                resolution = 0
                min = 0.0
                max = 1.0
            }
            slider("Vertical") {
                value = 50.0
                orientation = SliderOrientation.VERTICAL
            }
            slider("Vertical, sized") {
                value = 50.0
                orientation = SliderOrientation.VERTICAL
                h = 200.px
            }
            slider("Custom handle") {
                value = 50.0
                h = 200.px
                addStyleName("color1")
                orientation = SliderOrientation.VERTICAL
            }
            slider("Custom track") {
                value = 50.0
                h = 200.px
                addStyleName("color2")
                orientation = SliderOrientation.VERTICAL
            }
            slider("Custom indicator") {
                value = 50.0
                h = 200.px
                addStyleName("color3")
                orientation = SliderOrientation.VERTICAL
            }
            slider("No indicator") {
                value = 50.0
                h = 200.px
                addStyleName(ValoTheme.SLIDER_NO_INDICATOR)
                orientation = SliderOrientation.VERTICAL
            }
            slider("With ticks") {
                value = 3.0
                h = 200.px
                max = 4.0
                addStyleName("ticks")
                orientation = SliderOrientation.VERTICAL
            }
            slider("Disabled") {
                value = 50.0
                isEnabled = false
            }
        }
        label("Progress Bars") { addStyleName(ValoTheme.LABEL_H1) }
        horizontalLayout {
            addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING)
            pb = progressBar {
            caption = "Default"
            w = 300.px
            }
            pb2 = progressBar("Point style") {
                w = 300.px
                addStyleName(ValoTheme.PROGRESSBAR_POINT)
            }
            progressBar("Indeterminate") {
                isIndeterminate = true
            }
        }
    }

    private var progress = 0f

    @Transient
    private val update: Thread = object : Thread() {
        override fun run() {
            try {
                while (true) {
                    try {
                        Thread.sleep(1000)
                        ui.access {
                            pb.value = progress
                            pb2.value = progress
                            if (progress > 1) {
                                progress = 0f
                            } else {
                                progress += (0.2 * Math.random()).toFloat()
                            }
                        }
                    } catch (e: InterruptedException) {
                        break
                    }
                }
            } catch (t: Throwable) {
                log.error("Update failed: $t", t)
            }
        }
    }

    override fun enter(event: ViewChangeEvent) {
        ui.pollInterval = 1000
        update.start()
    }

    override fun detach() {
        ui.pollInterval = -1
        update.interrupt()
        super.detach()
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(Sliders::class.java)
    }
}
