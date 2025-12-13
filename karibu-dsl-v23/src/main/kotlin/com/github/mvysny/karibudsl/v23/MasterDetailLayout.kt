package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.buildSingleComponent
import com.github.mvysny.karibudsl.v10.init
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.masterdetaillayout.MasterDetailLayout

/**
 * Creates a [MasterDetailLayout](https://vaadin.com/docs/latest/components/master-detail-layout). Example of usage:
 * ```kotlin
 * val masterDetail = masterDetailLayout {
 *   detailSize = "400px"
 *   master {
 *     grid<Department>(Departments.dataProvider) {
 *       // ...
 *       asSingleSelect().addValueChangeListener { showDepartment(it.value) }
 *     }
 *   }
 * }
 * fun showDepartment(dept: Department?) {
 *   if (dept == null) {
 *     masterDetail.detail = null
 *   } else {
 *     masterDetail.detail {
 *       verticalLayout {
 *         departmentForm(dept)
 *         button("Edit") {
 *           // ...
 *         }
 *       }
 *     }
 *   }
 * }
 * ```
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).masterDetailLayout(block: (@VaadinDsl MasterDetailLayout).() -> Unit = {}): MasterDetailLayout
        = init(MasterDetailLayout(), block)

/**
 * Populates [MasterDetailLayout.setMaster], DSL-style. See [masterDetailLayout] for an example.
 */
@VaadinDsl
public fun <C : Component> (@VaadinDsl MasterDetailLayout).master(block: (@VaadinDsl HasComponents).() -> C): C {
    master = buildSingleComponent("MasterDetailLayout.master{}", block)
    @Suppress("UNCHECKED_CAST")
    return master as C
}

/**
 * Populates [MasterDetailLayout.setDetail], DSL-style. See [masterDetailLayout] for an example.
 */
@VaadinDsl
public fun <C : Component> (@VaadinDsl MasterDetailLayout).detail(block: (@VaadinDsl HasComponents).() -> C): C {
    detail = buildSingleComponent("MasterDetailLayout.detail{}", block)
    @Suppress("UNCHECKED_CAST")
    return detail as C
}
