package com.base.hilt.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import java.io.*
import java.nio.channels.FileChannel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object FileUtils {
    val allowedVideoFileExtensions =
        arrayOf("mpv", "mp4", "3gp", "3gpp", "mov", "m4v", "webm", "mpeg")
    private const val LOG_TAG = "System out"

    /**
     * get File path for Image & Thumb image
     *
     * @
     */
    internal fun getOutputMediaFile(
        mContext: Context,
        isForThumb: Boolean,
        fileName: String?
    ): File {
        var fileName = fileName

//        val mediaStorageDir =Environment.getExternalStorageDirectory()
        val mediaStorageDir = mContext.getExternalFilesDir(null)!!
        mediaStorageDir.mkdirs()

        /*Create a media file name*/
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val timeStamp = System.currentTimeMillis().toString()
        val mediaFile: File
        if (isForThumb) {
            if (!isFileNameHaveExtension(fileName)) {
                fileName += THUMB_EXT
            }
            mediaFile = File(mediaStorageDir.path + File.separator + timeStamp + fileName)
            //                    /*"IMG_Thumb" + */fileName /**/); // filename will be with ext.
        } else {
            mediaFile = File(
                mediaStorageDir.path + File.separator +
                        "IMG_" + /*convertStringToMd5(HttpCommonMethod.getUUID(mContext) +*/ timeStamp/*)*/ + ".jpg"
            )
        }

        return mediaFile
    }

    /**
     * get file dirctory name for video
     */
    fun getVideoDirectory(mContext: Context): File? {
        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ), Utils.getApplicationName(mContext) + "/Video"
        )

        /*Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            return null
        }
        return mediaStorageDir
    }

    /**
     * get File path for Video
     *
     * @return File name for video
     */
    fun makeFileForVideo(mContext: Context): File? {

        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ), Utils.getApplicationName(mContext) + "/Video"
        )

        /*Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            return null
        }

        /*Create a media file name*/
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())

        return File(
            mediaStorageDir.path + File.separator +
                    "VID_" + /*convertStringToMd5(HttpCommonMethod.getUUID(mContext) +*/ timeStamp/*)*/ + ".mp4"
        )
    }

    fun createVideoThumb(mContext: Context, filePath: String): String {
        val THUMBNAIL_WIDTH = 500 // change as per need
        val THUMBNAIL_HEIGHT = 500 // change as per need
        var bitmap =
            ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MINI_KIND)
        if (bitmap != null) {

            bitmap =
                ThumbnailUtils.extractThumbnail(
                    bitmap,
                    THUMBNAIL_WIDTH,
                    THUMBNAIL_HEIGHT
                )

            val fileOutputStream: FileOutputStream
            val savedBtimapFilePath = getOutputMediaFile(mContext, true, "")!!.absolutePath
            fileOutputStream = FileOutputStream(savedBtimapFilePath)

            bitmap.compress(Bitmap.CompressFormat.PNG, 60, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            DebugLog.e("Video Thumb:$savedBtimapFilePath")
            return savedBtimapFilePath
        } else return ""
    }

    /**
     * Create Thumbnail image
     *
     * @param mContext Context
     * @param filePath file path of original image
     * @param fileName File name which will be assign to thumbnail image
     * @return thumbnail path
     */
    fun createThumbnailFromFile(mContext: Context, filePath: String, fileName: String): String {
        val THUMBNAIL_WIDTH = 500 // change as per need
        val THUMBNAIL_HEIGHT = 500 // change as per need

        val b = BitmapFactory.decodeFile(filePath)
        var resized_bitmap: Bitmap
        var savedBtimapFilePath: String
        try {
            if (b != null) {
                val origWidth = b.width
                val origHeight = b.height

                val destWidth = 500f // or the width you need
                resized_bitmap = if (origWidth > destWidth) {
                    // picture is wider than we want it, we calculate its target height
                    //                        int destHeight = origHeight/( origWidth / destWidth ) ;
                    val calculatedWidth = destWidth / origWidth
                    val destHeight1 = (origHeight * calculatedWidth).toInt()
                    Bitmap.createScaledBitmap(b, destWidth.toInt(), destHeight1, false)
                } else {
                    val destHeight1 = (origHeight / (origWidth / destWidth)).toInt()
                    Bitmap.createScaledBitmap(b, destWidth.toInt(), destHeight1, false)
                }
            } else {
                resized_bitmap = ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(filePath),
                    THUMBNAIL_WIDTH,
                    THUMBNAIL_HEIGHT
                )
            }
        } catch (e: Exception) {
            DebugLog.print(e)
            resized_bitmap =
                ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(filePath),
                    THUMBNAIL_WIDTH,
                    THUMBNAIL_HEIGHT
                )
        }

        try {
            savedBtimapFilePath = getOutputMediaFile(mContext, true, fileName)!!.absolutePath
            val fileOutputStream = FileOutputStream(savedBtimapFilePath)

            resized_bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: Exception) {
            DebugLog.print(e)
            savedBtimapFilePath = filePath
        }

        return savedBtimapFilePath
    }


    /*   */
    /**
     * Compress Original image
     *
     * @param filePath original image file path
     * @param mContext Context
     * @return compressed Image file path
     *//*
    fun compressOriginalImage(mContext: Context, filePath: String): String {
        val compressedFilePath: String//= filePath;

        val compressor =
            Compressor(mContext).setQuality(75).setCompressFormat(Bitmap.CompressFormat.JPEG)
        compressedFilePath = try {
            compressor.compressToFile(File(filePath)).absolutePath
        } catch (e: IOException) {
            DebugLog.print(e)
            filePath
        }

        DebugLog.e("CompressPath$compressedFilePath")
        return compressedFilePath
    }
*/
    /**
     * copy file to specific file
     *
     * @param sourceFile source file
     * @param destFile Destination file
     */
    @Throws(IOException::class)
    fun copyFile(sourceFile: File, destFile: File) {
        if (!destFile.parentFile.exists())
            destFile.parentFile.mkdirs()

        if (!destFile.exists()) {
            destFile.createNewFile()
        }

        var source: FileChannel? = null
        var destination: FileChannel? = null

        try {
            source = FileInputStream(sourceFile).channel
            destination = FileOutputStream(destFile).channel
            destination!!.transferFrom(source, 0, source!!.size())
        } catch (e: Exception) {
            DebugLog.print(e)
        } finally {
            source?.close()
            destination?.close()
        }
    }

    /**
     * Delete file from storage
     */
    fun deleteFile(filePath: String) {
        val fdelete = File(filePath)
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                DebugLog.print("file Deleted :$filePath")
            } else {
                DebugLog.print("file not Deleted :$filePath")
            }
        }
    }

    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun getMimeType(context: Context, uri: Uri): String? {
        return if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
            val cr = context.contentResolver
            cr.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase())
        }
    }

    /**
     * store Bitmap in file
     */
    fun writeBitmapImageToFile(image: Bitmap, pictureFile: File?) {
        if (pictureFile == null) {
            DebugLog.print(
                LOG_TAG,
                "Error creating media file, check storage permissions: "
            ) // e.getMessage());
            return
        }
        try {
            val fos = FileOutputStream(pictureFile)
            image.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
        } catch (e: FileNotFoundException) {
            DebugLog.print(LOG_TAG, "File not found: " + e.message)
        } catch (e: IOException) {
            DebugLog.print(LOG_TAG, "Error accessing file: " + e.message)
        }
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

    /**
     * Check whether filename have extension
     *
     * @param fileName filename as String
     * @return true/false
     */

    private fun isFileNameHaveExtension(fileName: String?): Boolean {
        return fileName!!.endsWith("jpg") || fileName.endsWith("jpeg") || fileName.endsWith("JPEG") || fileName.endsWith(
            "JPG"
        ) || fileName.endsWith(
            "PNG"
        ) || fileName.endsWith("png") || fileName.endsWith("mp4") || fileName.endsWith("MP4")
    }

    var THUMB_SUFFIX = "thumb_" // "350x%s_";
    private var THUMB_EXT = ".jpeg"

    /**
     * Method for return file path of Gallery image
     *
     * @return path of the selected image file from gallery
     */
    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun getPath(context: Context, uri: Uri): String? {

        // check here to KITKAT or new version
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        when {
            isKitKat && DocumentsContract.isDocumentUri(context, uri) -> // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }
                } else if (isDownloadsDocument(uri)) {

                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )

                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    var contentUri: Uri? = null
                    when (type) {
                        "image" -> {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        }
                        "video" -> {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        }
                        "audio" -> {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }
                    }

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])

                    return getDataColumn(context, contentUri, selection, selectionArgs)
                } // MediaProvider
            // DownloadsProvider
            "content".equals(uri.scheme!!, ignoreCase = true) -> // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                    context,
                    uri,
                    null,
                    null
                )
            "file".equals(uri.scheme!!, ignoreCase = true) -> return uri.path
        } // File
        // MediaStore (and general)
        // File
        // MediaStore (and general)

        return null
    }

    /**
     * Get the value of the data column for this Uri. This is useful for MediaStore Uris, and other
     * file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor =
                context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }


    /*
     /**
     * Allows to fix issue for some phones when image processed with android-crop is not rotated
     * properly. Based on https://github.com/jdamcd/android-crop/issues/140#issuecomment-125109892
     *
     * @param context - context to use while saving file
     * @param uri - origin file uri
     */
    fun normalizeImageForUri(context: Context, uri: Uri) {
        try {
            val exif = ExifInterface(uri.path)
            val orientation =
                exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
                )
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            val rotatedBitmap: Bitmap
            if (bitmap != null) {
                rotatedBitmap = rotateBitmap(bitmap, orientation)
                if (bitmap != rotatedBitmap) {
                    saveBitmapToFile(context, rotatedBitmap, uri)
                }
            }

        } catch (e: Exception) {
            DebugLog.print(e)
        }

    }*/
/*
    private fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> return bitmap
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
            else -> return bitmap
        }
        return try {
            val bmRotated =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            bitmap.recycle()

            bmRotated
        } catch (e: OutOfMemoryError) {
            bitmap
        }

    }*/

    private fun saveBitmapToFile(context: Context, croppedImage: Bitmap, saveUri: Uri?) {
        if (saveUri != null) {
            var outputStream: OutputStream? = null
            try {
                outputStream = context.contentResolver.openOutputStream(saveUri)
                if (outputStream != null) {
                    croppedImage.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                }
            } catch (e: IOException) {
                DebugLog.printE("Cannot open file: %s | %s", saveUri.toString() + "::" + e.message)
            } finally {
                closeSilently(outputStream)
                croppedImage.recycle()
            }
        }
    }

    private fun closeSilently(c: Closeable?) {
        if (c == null) return
        try {
            c.close()
        } catch (t: Throwable) {
            // Do nothing
        }
    }

    fun getFileName(path: String): String {
        val arrFileName = path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        return arrFileName[arrFileName.size - 1]
    }

    fun getFileNameFromURI(mContext: Context, uri: Uri): String {
        val uriString = uri.toString()
        val myFile = File(uriString)
        val path = myFile.absolutePath
        var displayName: String? = null

        if (uriString.startsWith("content://")) {
            var cursor: Cursor? = null
            try {
                cursor = mContext.contentResolver.query(uri, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    displayName =
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
        } else if (uriString.startsWith("file://")) {
            displayName = myFile.name
        }
        return path
    }

    fun createDirectoryAndSaveFile(mContext: Context, filePath: String?): String? {
        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ), Utils.getApplicationName(mContext) + "/Image"
        )

        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs()
        }

        /*Create a media file name*/
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val mediaFile = File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".jpg")

        val imageToSave = BitmapFactory.decodeFile(filePath)
        try {
            val out = FileOutputStream(mediaFile)
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 75, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mediaFile.absolutePath
    }

    /**
     * return file size in MB
     * */
    fun getFileSize(path: String): Long {
        val file = File(path)
        val fileSizeInBytes = file.length()
        val fileSizeInKB = fileSizeInBytes / 1024
        return fileSizeInKB / 1024
    }

    /**
     * return file size in MB
     * */
    fun getFileSizeInKbMb(path: String): String {
        val file = File(path)
        val fileSizeInBytes = file.length()
        val fileSizeInKB = fileSizeInBytes / 1024
        return if (fileSizeInKB > 1024) {
            (fileSizeInKB / 1024).toString().plus(" MB")
        } else {
            fileSizeInKB.toString().plus(" KB")
        }
    }

    /**
     * get File path for Video
     *
     * @return File name for video
     */
    fun checkDocumentExist(mContext: Context, fileName: String?): Boolean? {
        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS
            ), Utils.getApplicationName(mContext)
        )

        /*Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            return null
        }

        /*Create a media file name*/
        val mediaFile = File(
            mediaStorageDir.path + File.separator + fileName
        )
        return mediaFile.exists()
    }

    /**
     * get File path for Video
     *
     * @return File name for video
     */
    fun getDocumentFile(mContext: Context, fileName: String?): File? {
        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS
            ), Utils.getApplicationName(mContext)
        )

        /*Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            return null
        }

        /*Create a media file name*/
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    /**
     * return file size in MB
     * */
    fun getFileSizeWithExtension(size: Int): String {
        val decimalFormat = DecimalFormat("#.##")
        val sizeofMB = 1024 * 1024
        val sizeofKb = 1024
        if (size > sizeofMB) {
            return decimalFormat.format(size / sizeofMB) + "MB"
        }
        if (size > sizeofKb) {
            return decimalFormat.format(size / sizeofKb) + "KB"
        }
        return decimalFormat.format(size) + "B"
    }
}
