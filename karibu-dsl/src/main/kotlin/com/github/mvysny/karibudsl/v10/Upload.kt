package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.upload.Receiver
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.UploadI18N

/**
 * [Upload](https://vaadin.com/docs/latest/components/upload) allows the user to upload files, giving feedback to the user during the upload process. It shows the upload progress and the status of each file. Files can be uploaded by clicking on the Upload button, or by dragging them onto the component.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).upload(receiver: Receiver? = null, block: (@VaadinDsl Upload).() -> Unit = {}): Upload
        = init(Upload(receiver), block)

/**
 * Assists with buildup of the Upload's i18n configuration:
 * ```
 * upload {
 *     i18n {
 *         dropFiles {
 *             many = "Foo"
 *         }
 *         addFiles {
 *             one = "Bar"
 *         }
 *         error {
 *             fileIsTooBig = "Baz"
 *         }
 *         uploading {
 *             status {
 *                 connecting = "Foo connecting"
 *             }
 *             remainingTime {
 *                 prefix = "Bar prefix"
 *             }
 *             error {
 *                 forbidden = "Baz forbidden"
 *             }
 *         }
 *     }
 * }
 * ```
 */
@VaadinDsl
public fun (@VaadinDsl Upload).i18n(block: (@VaadinDsl UploadI18N).() -> Unit): UploadI18N {
    if (i18n == null) i18n = UploadI18N()
    i18n.block()
    return i18n
}

@VaadinDsl
public fun (@VaadinDsl UploadI18N).dropFiles(block: (@VaadinDsl UploadI18N.DropFiles).() -> Unit): UploadI18N.DropFiles {
    if (dropFiles == null) dropFiles = UploadI18N.DropFiles()
    dropFiles.block()
    return dropFiles
}

@VaadinDsl
public fun (@VaadinDsl UploadI18N).addFiles(block: (@VaadinDsl UploadI18N.AddFiles).() -> Unit): UploadI18N.AddFiles {
    if (addFiles == null) addFiles = UploadI18N.AddFiles()
    addFiles.block()
    return addFiles
}

@VaadinDsl
public fun (@VaadinDsl UploadI18N).error(block: (@VaadinDsl UploadI18N.Error).() -> Unit): UploadI18N.Error {
    if (error == null) error = UploadI18N.Error()
    error.block()
    return error
}

@VaadinDsl
public fun (@VaadinDsl UploadI18N).uploading(block: (@VaadinDsl UploadI18N.Uploading).() -> Unit): UploadI18N.Uploading {
    if (uploading == null) uploading = UploadI18N.Uploading()
    uploading.block()
    return uploading
}

@VaadinDsl
public fun (@VaadinDsl UploadI18N.Uploading).status(block: (@VaadinDsl UploadI18N.Uploading.Status).() -> Unit): UploadI18N.Uploading.Status {
    if (status == null) status = UploadI18N.Uploading.Status()
    status.block()
    return status
}

@VaadinDsl
public fun (@VaadinDsl UploadI18N.Uploading).remainingTime(block: (@VaadinDsl UploadI18N.Uploading.RemainingTime).() -> Unit): UploadI18N.Uploading.RemainingTime {
    if (remainingTime == null) remainingTime = UploadI18N.Uploading.RemainingTime()
    remainingTime.block()
    return remainingTime
}

@VaadinDsl
public fun (@VaadinDsl UploadI18N.Uploading).error(block: (@VaadinDsl UploadI18N.Uploading.Error).() -> Unit): UploadI18N.Uploading.Error {
    if (error == null) error = UploadI18N.Uploading.Error()
    error.block()
    return error
}

/**
 * Creates the [Upload.uploadButton] in a DSL fashion:
 * ```kotlin
 * upload {
 *   uploadButton { span("Hello!") }
 * }
 * ```
 */
@VaadinDsl
public fun (@VaadinDsl Upload).uploadButton(block: (@VaadinDsl HasComponents).() -> Unit) {
    element.removeAllChildren()
    uploadButton = provideSingleComponent(block)
}

