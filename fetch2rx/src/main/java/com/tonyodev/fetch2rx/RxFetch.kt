package com.tonyodev.fetch2rx

import android.annotation.SuppressLint
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2.exception.FetchException
import com.tonyodev.fetch2.fetch.FetchModulesBuilder
import com.tonyodev.fetch2.Status
import com.tonyodev.fetch2.util.DEFAULT_ENABLE_LISTENER_NOTIFY_ON_ATTACHED
import com.tonyodev.fetch2core.DownloadBlock
import com.tonyodev.fetch2core.FileResource
import com.tonyodev.fetch2core.GLOBAL_FETCH_CONFIGURATION_NOT_SET

/**
 * A light weight file download manager for Android with Rx features.
 * Features: Background downloading,
 *           Queue based Priority downloading,
 *           Pause & Resume downloads,
 *           Network specific downloading and more...
 * @see https://github.com/tonyofrancis/Fetch
 * */
interface RxFetch {

    /** Returns true if this instance of fetch is closed and cannot be reused.*/
    val isClosed: Boolean

    /** The namespace which this instance of fetch operates in. An app can
     * have several instances of Fetch with different namespaces.
     * @see com.tonyodev.fetch2.FetchConfiguration
     * */
    val namespace: String

    /**
     * Queues a request for downloading. If Fetch fails to enqueue the request,
     * func2 will be called with the error.
     * Errors that may cause Fetch to fail the enqueue are :
     * 1. No storage space on the device.
     * 2. Fetch is already managing the same request. This means that a request with the same url
     * and file name is already managed.
     * @param request Download Request
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with request result.
     * Fetch may update a request depending on the initial request's Enqueue Action.
     * Update old request references with this request.
     * */
    fun enqueue(request: Request): Convertible<Request>

    /**
     * Queues a list of requests for downloading. If Fetch fails to enqueue a
     * download request because an error occurred, all other request in the list will
     * fail. Func2 will be called with the error message.
     * Errors that may cause Fetch to fail the enqueue are :
     * 1. No storage space on the device.
     * 2. Fetch is already managing the same request. This means that a request with the same url
     * and file name is already managed.
     * @param requests Request List
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with requests list.
     * Fetch may update a request depending on the initial request's Enqueue Action.
     * Update old request references with this request.
     * */
    fun enqueue(requests: List<Request>): Convertible<List<Request>>

    /** Pause a queued or downloading download.
     * @param ids ids of downloads to be paused.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with list of paused downloads. Note. Only downloads that
     * were paused will be returned in the result list.
     * */
    fun pause(ids: List<Int>): Convertible<List<Download>>

    /** Pause a queued or downloading download.
     * @param id id of download to be paused.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with paused download if successful otherwise null.
     * */
    fun pause(id: Int): Convertible<Download>

    /**
     * Pause all queued or downloading downloads within the specified group.
     * @param id specified group id.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with list of downloads that was paused.
     * */
    fun pauseGroup(id: Int): Convertible<List<Download>>

    /** Pauses all currently downloading items, and pauses all download processing fetch operations.
     *  Use this method when you do not want Fetch to keep processing downloads
     *  but do not want to release the instance of Fetch. However, you are still able to query
     *  download information.
     *  @see unfreeze
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with action results. True if freeze otherwise false.
     * */
    fun freeze(): Convertible<Boolean>

    /** Resume a download that has been paused.
     * @param ids ids of downloads to be resumed.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with list of downloads that was successfully resumed.
     * */
    fun resume(ids: List<Int>): Convertible<List<Download>>

    /** Resume a download that has been paused.
     * @param id id of download to be resumed.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with resumed download that was paused.
     * */
    fun resume(id: Int): Convertible<Download>

    /**
     * Resume all paused downloads within the specified group.
     * @param id specified group id.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with list of resumed downloads.
     * */
    fun resumeGroup(id: Int): Convertible<List<Download>>

    /** Allow fetch to resume operations after freeze has been called.
     * @see freeze
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with action results. True if unfreeze otherwise false.
     * */
    fun unfreeze(): Convertible<Boolean>

    /**
     * Remove a list of downloads managed by this instance of Fetch.
     * The downloaded file for the removed downloads are not deleted.
     * @param ids ids of downloads to be removed.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with list of downloads that were removed.
     * */
    fun remove(ids: List<Int>): Convertible<List<Download>>

    /**
     * Remove a download managed by this instance of Fetch.
     * The downloaded file for the removed download is not deleted.
     * @param id id of download to be removed.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with download that was removed if successful.
     * */
    fun remove(id: Int): Convertible<Download>

    /**
     * Remove all downloads in the specified group managed by this instance of Fetch.
     * The downloaded files for removed downloads are not deleted.
     * @param id specified group id
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with list of downloads that were removed.
     * */
    fun removeGroup(id: Int): Convertible<List<Download>>

    /**
     * Remove all downloads managed by this instance of Fetch.
     * The downloaded files for removed downloads are not deleted.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with list of downloads that were removed.
     * */
    fun removeAll(): Convertible<List<Download>>

    /**
     * Remove all downloads with the specified status in this instance of Fetch.
     * The downloaded files for removed downloads are not deleted.
     * @param status status
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with list of downloads that were removed.
     * */
    fun removeAllWithStatus(status: Status): Convertible<List<Download>>

    /**
     * Remove all downloads with the specified group and status in this instance of Fetch.
     * The downloaded files for removed downloads are not deleted.
     * @param status status
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with list of downloads that were removed.
     * */
    fun removeAllInGroupWithStatus(id: Int, status: Status): Convertible<List<Download>>

    /**
     * Delete a list of downloads managed by this instance of Fetch.
     * The downloaded file is deleted.
     * @param ids ids of downloads to be deleted.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with list of downloads that were deleted.
     * */
    fun delete(ids: List<Int>): Convertible<List<Download>>

    /**
     * Delete a download managed by this instance of Fetch.
     * The downloaded file is deleted.
     * @param id id of download to be deleted.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with download that was deleted if successful.
     * */
    fun delete(id: Int): Convertible<Download>

    /**
     * Deletes all downloads in the specified group managed by this instance of Fetch.
     * The downloaded files are also deleted.
     * @param id specified group id
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with list of downloads that were deleted.
     * */
    fun deleteGroup(id: Int): Convertible<List<Download>>

    /**
     * Deletes all downloads managed by this instance of Fetch.
     * The downloaded files are deleted.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with list of downloads that were deleted.
     * */
    fun deleteAll(): Convertible<List<Download>>

    /**
     * Deletes all downloads with the specified status in this instance of Fetch.
     * The downloaded files are also deleted.
     * @param status status
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with list of downloads that were deleted.
     * */
    fun deleteAllWithStatus(status: Status): Convertible<List<Download>>

    /**
     * Deletes all downloads with the specified group and status in this instance of Fetch.
     * The downloaded files are also deleted.
     * @param status status
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with list of downloads that were deleted.
     * */
    fun deleteAllInGroupWithStatus(id: Int, status: Status): Convertible<List<Download>>

    /**
     * Cancel a list of non completed downloads managed by this instance of Fetch.
     * The downloaded file for the cancelled download is not deleted.
     * @param ids ids of downloads to be cancelled.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with list of downloads that were cancelled.
     * */
    fun cancel(ids: List<Int>): Convertible<List<Download>>

    /**
     * Cancel a non completed download managed by this instance of Fetch.
     * The downloaded file for the cancelled download is not deleted.
     * @param id id of downloads to be cancelled.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with download that was cancelled if successful.
     * */
    fun cancel(id: Int): Convertible<Download>

    /**
     * Cancels all non completed downloads in the specified group managed by this instance of Fetch.
     * The downloaded files for cancelled downloads are not deleted.
     * @param id specified group id
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with list of downloads that were cancelled.
     * */
    fun cancelGroup(id: Int): Convertible<List<Download>>

    /**
     * Cancels all non completed downloads managed by this instance of Fetch.
     * The downloaded files for cancelled downloads are not deleted.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with list of downloads that were cancelled.
     * */
    fun cancelAll(): Convertible<List<Download>>

    /**
     * Retries to download a list of failed or cancelled downloads.
     * @param ids ids of the failed or cancelled downloads.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with the list of downloads that were successfully queued.
     * */
    fun retry(ids: List<Int>): Convertible<List<Download>>

    /**
     * Retries to download a failed or cancelled download.
     * @param id id of the failed or cancelled downloads.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with the download that was successfully queued or null.
     * */
    fun retry(id: Int): Convertible<Download>

    /** Updates an existing request.
     * @see com.tonyodev.fetch2.Request for more details.
     * @param requestId Id of existing request/download
     * @param updatedRequest Request object
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with the successfully updated download or null.
     * */
    fun updateRequest(requestId: Int, updatedRequest: Request): Convertible<Download>

    /**
     * Gets all downloads managed by this instance of Fetch.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with results.
     * */
    fun getDownloads(): Convertible<List<Download>>

    /**
     * Gets the downloads which match an id in the list. Only successful matches will be returned.
     * @param idList Id list to perform id query against.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with results.
     * */
    fun getDownloads(idList: List<Int>): Convertible<List<Download>>

    /**
     * Gets the download which has the specified id. If the download
     * does not exist null will be returned.
     * @param id Download id
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with result.
     * */
    fun getDownload(id: Int): Convertible<Download?>

    /**
     * Gets all downloads in the specified group.
     * @param groupId group id to query.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with results.
     * */
    fun getDownloadsInGroup(groupId: Int): Convertible<List<Download>>

    /**
     * Gets all downloads with a specific status.
     * @see com.tonyodev.fetch2.Status
     * @param status Status to query.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with results.
     * */
    fun getDownloadsWithStatus(status: Status): Convertible<List<Download>>

    /**
     * Gets all downloads in a specific group with a specific status.
     * @see com.tonyodev.fetch2.Status
     * @param groupId group id to query.
     * @param status Status to query.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with results.
     * */
    fun getDownloadsInGroupWithStatus(groupId: Int, status: Status): Convertible<List<Download>>

    /**
     * Gets all downloads containing the identifier.
     * @param identifier identifier.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with results.
     * */
    fun getDownloadsByRequestIdentifier(identifier: Long): Convertible<List<Download>>

    /** Attaches a FetchListener to this instance of Fetch.
     * @param listener Fetch Listener
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Instance
     * */
    fun addListener(listener: FetchListener): RxFetch

    /** Attaches a FetchListener to this instance of Fetch.
     * @param listener Fetch Listener
     * @param notify Allows Fetch to notify the newly attached listener instantly of the download status
     * of all downloads managed by the namespace. Default is false.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Instance
     * */
    fun addListener(listener: FetchListener, notify: Boolean = DEFAULT_ENABLE_LISTENER_NOTIFY_ON_ATTACHED): RxFetch

    /** Detaches a FetchListener from this instance of Fetch.
     * @param listener Fetch Listener
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Instance
     * */
    fun removeListener(listener: FetchListener): RxFetch

    /**
     * Adds a completed download to Fetch for management. If Fetch is already managing another download with the same file as this completed download's
     * file, Fetch will replace the already managed download with this completed download.
     * @param completedDownload Completed Download
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Instance
     * */
    fun addCompletedDownload(completedDownload: CompletedDownload): Convertible<Download>

    /**
     * Adds a list of completed downloads to Fetch for management. If Fetch is already managing another download with the same file as this completed download's
     * file, Fetch will replace the already managed download with this completed download.
     * @param completedDownloads Completed Downloads list
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible
     * */
    fun addCompletedDownloads(completedDownloads: List<CompletedDownload>): Convertible<List<Download>>

    /**
     * Gets the list of download blocks belonging to a download. List may be empty if
     * blocks could not be found for the download id or download has never been processed.
     * @param downloadId: Download ID
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible
     * */
    fun getDownloadBlocks(downloadId: Int): Convertible<List<DownloadBlock>>

    /**
     * Enable or disable logging.
     * @param enabled Enable or disable logging.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Instance
     * */
    fun enableLogging(enabled: Boolean): RxFetch

    /**
     * Overrides each downloads specific network type preference and uses a
     * global network type preference instead.
     * Use com.tonyodev.fetch2.NetworkType.GLOBAL_OFF to disable the global network preference.
     * The default value is com.tonyodev.fetch2.NetworkType.GLOBAL_OFF
     * @see com.tonyodev.fetch2.NetworkType
     * @param networkType The global network type.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Instance
     * */
    fun setGlobalNetworkType(networkType: NetworkType): RxFetch

    /** Sets the number of parallel downloads Fetch should perform at any given time.
     * Default value is 1. This method can only accept values greater than 0. Setting
     * concurrent limit to zero prevents the instance of Fetch to pull and download request
     * from the waiting queue but allows the instance of Fetch to act on and observe changes to
     * requests/downloads.
     * @param downloadConcurrentLimit Number of parallel downloads.
     * @throws FetchException if the passed in download concurrent limit is less than 0 or
     * Fetch instance has been closed.
     * @return Instance
     * */
    fun setDownloadConcurrentLimit(downloadConcurrentLimit: Int): RxFetch

    /** Releases held resources and the namespace used by this Fetch instance.
     * Once closed this instance cannot be reused but the namespace can be reused
     * by a new instance of Fetch.
     * @throws FetchException if this instance of Fetch has been closed.
     * */
    fun close()

    /**
     * Gets the content Length for a request. If the request or contentLength cannot be found in
     * the Fetch database(meaning Fetch never processed the request and started downloading it) -1 is returned.
     * However, setting fromServer to true will create a new connection to the server to get the connectLength
     * if Fetch does not already contain the data in the database for the request.
     * @param request Request. Can be a managed or un-managed request. The request is not stored in
     * the fetch database.
     * connection to get the contentLength
     * @param fromServer If true, fetch will attempt to get the ContentLength
     * from the server directly by making a network request. Otherwise no action is taken.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with content length result. If value is -1. This means that Fetch was
     * not able to get the content length.
     * */
    fun getContentLengthForRequest(request: Request, fromServer: Boolean): Convertible<Long>

    /**
     * Gets the full Catalog of a Fetch File Server.
     * @param request Request. Can be a managed or un-managed request. The request is not stored in
     * the fetch database.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return Convertible with catalog results.
     * */
    fun getFetchFileServerCatalog(request: Request): Convertible<List<FileResource>>

    /**
     * RX Fetch implementation class. Use this Singleton to get instances of RxFetch or Fetch.
     * */
    companion object Impl {

        private val lock = Any()
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var defaultRxFetchConfiguration: FetchConfiguration? = null
        @Volatile
        private var defaultRxFetchInstance: RxFetch? = null

        /**
         * Sets the default Configuration settings on the default Fetch instance.
         * @param fetchConfiguration custom Fetch Configuration
         * */
        fun setDefaultRxInstanceConfiguration(fetchConfiguration: FetchConfiguration) {
            synchronized(lock) {
                defaultRxFetchConfiguration = fetchConfiguration
            }
        }

        /**
         * Get the default Fetch Configuration set with setDefaultInstanceConfiguration(fetchConfiguration: FetchConfiguration)
         * or setDefaultInstanceConfiguration(context: Context)
         * @return default FetchConfiguration
         * */
        fun getDefaultRxFetchConfiguration(): FetchConfiguration? {
            return synchronized(lock) {
                defaultRxFetchConfiguration
            }
        }

        /**
         * @throws FetchException if default FetchConfiguration is not set.
         * @return Get default RxFetch instance
         * */
        fun getDefaultRxInstance(): RxFetch {
            return synchronized(lock) {
                val rxFetchConfiguration = defaultRxFetchConfiguration
                        ?: throw FetchException(GLOBAL_FETCH_CONFIGURATION_NOT_SET)
                val defaultRxFetch = defaultRxFetchInstance
                if (defaultRxFetch == null || defaultRxFetch.isClosed) {
                    val newDefaultRxFetch = RxFetchImpl.newInstance(FetchModulesBuilder.buildModulesFromPrefs(rxFetchConfiguration))
                    defaultRxFetchInstance = newDefaultRxFetch
                    newDefaultRxFetch
                } else {
                    defaultRxFetch
                }
            }
        }

        /**
         * Creates a custom Instance of Fetch with the given configuration and namespace.
         * @param fetchConfiguration custom Fetch Configuration
         * @return custom RxFetch instance
         * */
        fun getRxInstance(fetchConfiguration: FetchConfiguration): RxFetch {
            return RxFetchImpl.newInstance(FetchModulesBuilder.buildModulesFromPrefs(fetchConfiguration))
        }

    }

}