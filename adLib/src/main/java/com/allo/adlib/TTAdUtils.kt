package com.allo.adlib

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.allo.data.Advertisement
import com.allo.data.bigdata.ClickData
import com.allo.data.bigdata.ExposureData
import com.allo.utils.LanguageUtils
import com.allo.view.dialog.MyProgressDialog
import com.bytedance.sdk.openadsdk.*


/**
 * @Author yforyoung
 * @Date 2021/7/9 14:39
 * @Desc 第三方广告整体工具类（目前包含快手广告sdk和穿山甲广告sdk）
 */
object TTAdUtils {
    private val TAG = TTAdUtils::class.java.simpleName

    private val ttAdManager: TTAdManager by lazy {
        TTAdSdk.getAdManager()
    }

    var appId = ""

    /**
     * 初始化广告SDK 目前有穿山甲SDK和快手SDK
     */
    fun init(app: Application, appId: String) {
        this.appId = appId
        try {
            OpenAdUtils.initOpenAdSDK(app, appId)
//            KsAdUtils.initKsAdSDK(app)
        } catch (e: Exception) {
            Log.e("===z", "init广告e=${e.message}")
        }
    }


    fun createAd(
        activity: Activity,
        adId: String,
        width: Int,
        height: Int,
        adType: AdType,
        listener: OnAdLoadListener,
    ) {

        when (adType) {
            AdType.SPLASH -> {

            }
            AdType.BANNER -> {
                createBannerAd(activity, adId, width, height, listener)
            }
            AdType.EXPRESS -> {
                createNativeAd(activity, adId, listener, width, height)

            }
            AdType.REWARD_VIDEO -> {

            }
            AdType.EXPRESS_DRAW -> {

            }
            AdType.INTERACTION -> {

            }
        }
    }

    private fun createBannerAd(
        activity: Activity,
        adId: String,
        width: Int,
        height: Int,
        listener: OnAdLoadListener
    ) {
//        val ttBannerAd = TTBannerViewAd(activity, adId)
//        ttBannerAd.setRefreshTime(30)
//        ttBannerAd.setAllowShowCloseBtn(false)
//        ttBannerAd.setTTAdBannerListener(object : TTAdBannerListener {
//            override fun onAdOpened() {}
//
//            override fun onAdLeftApplication() {
//            }
//
//            override fun onAdClosed() {
//                listener.onAdClose()
//            }
//
//            override fun onAdClicked() {
//                listener.onAdClick()
//            }
//
//            override fun onAdShow() {
//                listener.onAdShow()
//            }
//
//            override fun onAdShowFail(p0: AdError) {
//                listener.onAdLoadFailed(p0.message)
//            }
//
//
//
//        })
//        val adSlot = com.bytedance.msdk.api.AdSlot.Builder()
//            .setAdStyleType(com.bytedance.msdk.api.AdSlot.TYPE_EXPRESS_AD) // banner暂时只支持模版类型，必须手动设置为AdSlot.TYPE_EXPRESS_AD
//            .setBannerSize(TTAdSize.BANNER_CUSTOME) // 使用TTAdSize.BANNER_CUSTOME，可以通过setImageAdSize设置自定义大小
//            .setImageAdSize(width, height)
//            .setDownloadType(com.bytedance.msdk.api.TTAdConstant.DOWNLOAD_TYPE_POPUP)//下载合规设置
//            .build()
//        ttBannerAd.loadAd(adSlot, object : TTAdBannerLoadCallBack {
//            override fun onAdFailedToLoad(p0: AdError) {
//                listener.onAdLoadFailed(p0.message)
//            }
//
//            override fun onAdLoaded() {
//                if (ttBannerAd.bannerView != null) {
//                    listener.onAdLoadSuccess(ttBannerAd.bannerView)
//                } else {
//                    listener.onAdLoadFailed("暂未加载到广告")
//                }
//            }
//
//        })
    }

    private fun createNativeAd(
        activity: Activity,
        adId: String,
        listener: OnAdLoadListener,
        width: Int,
        height: Int
    ) {
//        val mInterstitialAd = TTUnifiedNativeAd(activity, adId)
//        val adSlot = com.bytedance.msdk.api.AdSlot.Builder()
//            .setAdStyleType(com.bytedance.msdk.api.AdSlot.TYPE_EXPRESS_AD) // 注意：插屏暂时支持模版类型，必须手动设置为AdSlot.TYPE_EXPRESS_AD
//            .setDownloadType(com.bytedance.msdk.api.TTAdConstant.DOWNLOAD_TYPE_POPUP)
//            .setAdCount(1)
//            .setImageAdSize(
//                width,
//                height
//            )    //根据广告平台选择的尺寸（目前该比例规格仅对穿山甲SDK生效，插屏广告支持的广告尺寸：  1:1, 3:2, 2:3）
//            .build()
//        mInterstitialAd.loadAd(adSlot, object : TTNativeAdLoadCallback {
//            override fun onAdLoaded(p0: MutableList<TTNativeAd>) {
//                if (p0.isNullOrEmpty()) {
//                    listener.onAdLoadFailed("未加载到广告")
//                    return
//                }
//                p0[0].let { ttNativeAd ->
//                    listener.onAdLoadSuccess(ttNativeAd.expressView)
//                    ttNativeAd.render()
//                    ttNativeAd.setTTNativeAdListener(object : TTNativeAdListener {
//                        override fun onAdClick() {
//                            listener.onAdClick()
//                        }
//
//                        override fun onAdShow() {
//                            listener.onAdShow()
//                        }
//
//                    })
//                    ttNativeAd.setDislikeCallback(activity, object : TTDislikeCallback {
//                        override fun onSelected(p0: Int, p1: String?) {
//                        }
//
//                        override fun onCancel() {
//                            listener.onAdClose()
//                        }
//
//                        override fun onRefuse() {
//                        }
//
//                        override fun onShow() {
//                        }
//
//                    })
//                }
//            }
//
//            override fun onAdLoadedFial(p0: AdError) {
//                listener.onAdLoadFailed(p0.message)
//            }
//
//
//
//        })
    }

    interface OnAdLoadListener {
        fun onAdLoadSuccess(adView: View?)
        fun onAdShow()
        fun onAdLoadFailed(msg: String?)
        fun onAdClick()
        fun onAdClose()
    }

    /**
     * 穿山甲原生广告回调（铃声列表）
     */
    interface OnTTNativeAdViewListener {
        fun onShow(ttNativeAd: TTNativeAd)
    }

    /**
     * 首页视频和铃声卡片广告回调(快手)
     */
    interface OnHomeNativeKsAdLoadListener {
//        fun onNativeAdLoad(ksNativeAd: KsNativeAd)
        fun onError(msg: String?)
    }

    /**
     * 首页视频和铃声卡片广告回调(穿山甲)
     */
    interface OnHomeNativeOpenAdLoadListener {
        fun onNativeAdLoad(ttFeedAd: TTFeedAd)
        fun onError(msg: String?)
    }


    /**
     * 开屏广告回调
     */
    interface OnSplashAdLoadListener {
        fun onAdSuccess()
        fun onAdShowStart()
        fun onAdShowEnd()
        fun onError(msg: String?)
        fun onSkippedAd()
        fun onAdClicked()
    }

    /**
     * Draw信息流广告回调
     */
    interface OnDrawAdLoadListener {
        fun onError(msg: String?)
        fun onAdClicked()
        fun onAdShow()
        fun onVideoPlayStart()
        fun onVideoPlayPause()
        fun onVideoPlayResume()
        fun onVideoPlayEnd()
        fun onVideoPlayError()
    }


    // 创建广告
    fun createAd(activity: Activity, adSlot: AdSlot, adType: AdType, listener: Any) {
        val adNative = ttAdManager.createAdNative(activity)
        when (adType) {
            AdType.SPLASH -> {
                if (listener is TTAdNative.SplashAdListener) {
                    adNative.loadSplashAd(adSlot, listener)
                }
            }
            AdType.BANNER -> {
                if (listener is TTAdNative.NativeExpressAdListener) {
                    adNative.loadBannerExpressAd(adSlot, listener)
                }

            }
            AdType.EXPRESS -> {
                if (listener is TTAdNative.NativeExpressAdListener) {
                    adNative.loadNativeExpressAd(adSlot, listener)
                }

            }
            AdType.REWARD_VIDEO -> {
                if (listener is TTAdNative.RewardVideoAdListener) {
                    adNative.loadRewardVideoAd(adSlot, listener)
                }
            }
            AdType.EXPRESS_DRAW -> {
                if (listener is TTAdNative.NativeExpressAdListener) {
                    adNative.loadExpressDrawFeedAd(adSlot, listener)
                }
            }
            AdType.INTERACTION -> {
                if (listener is TTAdNative.NativeExpressAdListener) {
                    adNative.loadInteractionExpressAd(adSlot, listener)
                }
            }
        }
    }


    /**
     * 获取快手Draw信息流广告 预览页上下滑动的广告
     */
    fun loadKsDrawAd(
        activity: Activity,
        adCode: String? = null,
        flItemAdContainer: FrameLayout,
        listener: OnDrawAdLoadListener
    ) {
//        KsAdUtils.loadKsDrawAd(activity, adCode, flItemAdContainer, listener)
    }

    /**
     * 获取原生广告
     */
    fun loadNativeAd(
        activity: Activity,
        adType: String? = null,
        adCode: String? = null,
        adContainer: ViewGroup,
        listener: OnAdLoadListener,
        isNeedWidth: Boolean? = null,
        width: Float? = null,
    ) {
        when (adType) {
            //穿山甲
            AdShowType.PANGLE -> {
                OpenAdUtils.loadOpenExpressAd(
                    activity,
                    adCode = adCode,
                    isNeedWidth = isNeedWidth,
                    adContainer = adContainer,
                    listener = listener,
                    width = width
                )
            }

            //快手
//            AdShowType.KUAISHOU -> KsAdUtils.loadKsConfigFeedAd(
//                activity,
//                adPosId = adCode,
//                adContainer,
//                listener,
//                isNeedWidth = true,
//                width = width,
//            )
        }

    }

    /**
     * 加载快手原生广告 --首页视频和壁纸
     * 广告选择 自渲染 竖屏图片 才能正常适配
     */
    fun loadKsNativeAd(adCode: String? = null, listener: OnHomeNativeKsAdLoadListener) {
//        KsAdUtils.loadKsNativeAd(adCode, listener)
    }


    /**
     * 获取插屏广告
     */
    fun loadInterstitialAd(
        adShowType: String? = null,
        adCode: String? = null,
        activity: Activity,
        listener: OnAdLoadListener? = null,
    ) {
        when (adShowType) {
            //穿山甲
            AdShowType.PANGLE -> {
                OpenAdUtils.loadOpenFullScreenVideoAd(adCode, activity, listener)
            }

            //快手
            AdShowType.KUAISHOU -> {
//                KsAdUtils.loadKsInterstitialAd(adCode, activity, listener)
            }
        }

    }


    /**
     * 获取激励视频
     */
    fun showRewardAd(
        adConfig: Advertisement,
        activity: Activity,
        pageId: String,
        listener: ((Boolean) -> Unit),
        showListener: (() -> Unit),
        adShowType: String? = null
    ) {
        //根据平台判断是加载快手还是穿山甲激励视频
        loadRewardVideoAd(activity, pageId, adConfig.adId, listener, showListener, adShowType)
    }


    /**
     * 获取激励视频广告
     */
    fun loadRewardVideoAd(
        activity: Activity,
        pageId: String,
        adCode: String? = null,
        listener: ((Boolean) -> Unit),
        showListener: (() -> Unit), adShowType: String? = null
    ) {
        when (adShowType) {
            //穿山甲
            AdShowType.PANGLE -> OpenAdUtils.loadOpenRewardVideoAd(
                activity,
                pageId,
                adCode,
                listener,
                showListener
            )

            //快手
//            AdShowType.KUAISHOU -> KsAdUtils.loadKsRewardVideoAd(
//                activity,
//                pageId,
//                adCode,
//                listener,
//                showListener
//            )
        }

    }

    /**
     * 加载开屏广告
     */
    fun loadSplashScreenAd(
        splashAdContainer: FrameLayout,
        context: Activity,
        adCode: String? = null,
        adShowType: String? = null,
        adLoadListener: OnSplashAdLoadListener
    ) {
        when (adShowType) {
            //穿山甲
            AdShowType.PANGLE -> {
                OpenAdUtils.loadOpenSplashScreenAd(
                    splashAdContainer,
                    adCode,
                    context,
                    adLoadListener
                )
            }

            //快手
            AdShowType.KUAISHOU -> {
//                KsAdUtils.loadKsSplashScreenAd(splashAdContainer, adCode, context, adLoadListener)
            }

            else -> {
                adLoadListener.onError("参数错误")
            }
        }
    }


    /**
     * 原有广告上报 advertisementBtn 被删除  目前能搜索到的都是已经停用的广告位
     */
    fun createClickData(
        pageId: String,
        adId: String,   // 广告位id
        adType: String  // 广告平台id
    ): ClickData {
        val map = mutableMapOf<String, String?>()

        map["platform"] = adType  // 平台
        map["advertiser"] = adType   //广告主  如果没有就使用 gromore/pangle

        map["creativityId"] = adType    //创意id
        map["creativityName"] = adType  // 创意name
        map["pageUrl"] = ""

        map["position"] = "0"

        map["columnId"] = ""
        map["columnName"] = ""
        map["instanceid"] = ""

        map["advertiserName"] = adType  // 广告主名称
        map["planId"] = adType  // 计划id
        map["planName"] = adType  // 计划名
        map["adId"] = adType  // 广告id
        map["adName"] = adType //广告名

        return ClickData(
            pageId = pageId,
            eventId = "event_click_ad", //页面Id+广告位Id
            eventType = "event_click_ad",
            targetId = adId,  // 广告id  如果没有就使用 gromore/pangle
            targetName = adId,     // 广告位名称  如果没有就使用 gromore/pangle
            targetType = "ad",
            extDataMap = map
        )
    }

    fun createExposureData(
        pageId: String,
        adId: String,
        adType: String
    ): ExposureData {
        val map = mutableMapOf<String, String?>()

        map["platform"] = adType  // 平台
        map["advertiser"] = adType   //广告主  如果没有就使用 gromore/pangle（广告平台id）

        map["creativityId"] = adType    //创意id
        map["creativityName"] = adType  // 创意name
        map["pageUrl"] = ""

        map["position"] = "0"

        map["columnId"] = ""
        map["columnName"] = ""
        map["instanceid"] = ""

        map["advertiserName"] = adType  // 广告主名称
        map["planId"] = adType  // 计划id
        map["planName"] = adType  // 计划名
        map["adId"] = adType  // 广告id  不同于广告位id和广告平台id  没有就使用广告平台id
        map["adName"] = adType //广告名
        map["eventType"] = "event_exposure_ad"

        return ExposureData(
            pageId = pageId,
            eventId = "event_exposure_ad",
            targetId = adId,  // 广告位id
            targetName = adId,     // 广告位名称  如果没有就使用广告位id
            targetType = "ad",
            extDataMap = map
        )
    }


    // 广告关闭弹窗
    fun bindDislikeDialog(activity: Activity, ad: TTNativeExpressAd, listener: (() -> Unit)) {
        ad.setDislikeDialog(DislikeAdDialog.create(activity).apply {
            dislikeListener = {
                listener.invoke()
            }
        })
        ad.setDislikeCallback(activity, object : TTAdDislike.DislikeInteractionCallback {
            override fun onShow() {

            }

            override fun onSelected(p0: Int, p1: String?, p2: Boolean) {
                listener.invoke()
            }

            override fun onCancel() {

            }

        })
    }

    //loading弹窗
    @SuppressLint("StaticFieldLeak")
    private var progressDialog: MyProgressDialog? = null
    fun showLoading(activity: Activity?) {
        activity ?: return
        try {
            //先销毁之前的实例
            hideLoading()
            progressDialog =
                MyProgressDialog.createDialog(
                    activity,
                    if (!LanguageUtils.isZh()) "يۈكلىنىۋاتىدۇ" else "加载中",
                    cancelable = true,
                    canTouchOutsideCancel = false
                )
            progressDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideLoading() {
        try {
            progressDialog?.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        progressDialog = null
    }

    enum class AdType {
        SPLASH,     // 开屏
        BANNER,     // banner
        EXPRESS,    // 信息流
        INTERACTION,   // 插屏
        REWARD_VIDEO,        // 激励视频
        EXPRESS_DRAW    // 全屏预览
    }

}


class AdCountDownTimer(
    private var startTime: Long,
    private val listener: ((AdCountDownTimer) -> Unit)
) {
    private var countDownTimer: CountDownTimer? = null

    // 倒计时
    private var mTime = 30 * 1000L

    init {
        mTime = startTime
    }

    fun start() {
        countDownTimer = object : CountDownTimer(mTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTime = millisUntilFinished
            }

            override fun onFinish() {
                listener.invoke(this@AdCountDownTimer)
            }
        }
        countDownTimer?.start()
    }

    fun stop() {
        countDownTimer?.cancel()
        countDownTimer = null
        mTime = startTime
    }

    fun pause() {
        countDownTimer?.cancel()
    }
}


object TTAdCons {

    //联系人顶部广告
    const val CONTACT = "contactGromore"

    //联系人搜索
    const val CONTACT_SEARCH = "contactSearchGromore"


    //图片签到
    const val PICTURE_SIGN = "pictureSignNew"

    //视频签到
    const val VIDEO_SIGN = "videoSignGromore"

    //设置来电秀观看的激励视频广告
    const val SET_CALL_SHOW_VIDEO = "setCallShowAdVideo"

    //通话领现金激励视频广告
    const val VIDEO_CALL_CASH = "videoCallCash"

    //通话领现金弹框广告
    const val CALL_CASH = "callCash"

    //切换tab的广告
    const val TAB_CHANGE = "tabChange"

    //联系人详情广告
    const val CONTACT_DETAIL = "contactDetail"

    //来电状态时的广告
    const val CALL_IN_COMING = "callInComing"

    //通话中的广告
    const val CALL_CALLING = "callInCalling"

    //预览视频加载广告
    const val PREVIEW_VIEW_LOADING = "previewLoading"

    //通话记录广告
    const val CALL_RECORD = "callRecord"

    //动态列表广告
    const val DYNAMIC_LIST = "dynamicList"


    /**************************1.8版本移除了以下广告位*******************************************/

    //首页视频预览 更换为自有广告平台下发
    const val HOME_PREVIEW = "homePreview"

    //收藏视频预览
    const val COLLECT_PREVIEW = "collectPreview"

    //下载视频预览
    const val DOWNLOAD_PREVIEW = "downloadPreview"

    //我的上传视频预览
    const val MY_UPLOAD_PREVIEW = "myUploadPreview"

    //用户上传视频预览
    const val OTHER_UPLOAD_PREVIEW = "otherUploadPreview"

    //启动页  更换为自有广告平台下发
    const val SPLASH = "splashGromore"

    //首页铃声列表    更换为自有广告平台广告id
    const val HOME_RING = "homeRingGromore"

    //用户上传铃声列表
    const val OTHER_UPLOAD_RING = "otherUploadRingGromore"

    //首页视频列表  更换为自有广告平台广告id
    const val HOME_VIDEO = "homeVideoGromore"

    //用户上传视频列表
    const val OTHER_UPLOAD_VIDEO = "otherUploadVideoGromore"

    // 专属联系人设置
    const val PERSONAL_SETTING = "personalSettingGromore"

    // 退出应用
    const val EXIT_APP = "quitAppNew"

    // 全屏预览加载广告
    const val PREVIEW_LOADING = "fullScreenPreviewLoadingGromore"

    //收藏视频列表
    const val COLLECT_VIDEO = "collectVideoGromore"

    //下载视频列表
    const val DOWNLOAD_VIDEO = "downloadVideoGromore"

    //我的上传视频列表
    const val MY_UPLOAD_VIDEO = "myUploadVideoGromore"

    //收藏铃声列表
    const val COLLECT_RING = "collectRingGromore"

    //下载铃声列表
    const val DOWNLOAD_RING = "downloadRingGromore"

    //我的上传铃声列表
    const val MY_UPLOAD_RING = "myUploadRingGromore"

    // 专题铃声广告
    const val RING_SPECIAL_TOPIC = "ringSpecialTopicGromore"

    // 专题视频广告
    const val VIDEO_SPECIAL_TOPIC = "videoSpecialTopicGromore"

    // 专题壁纸广告
    const val WALL_PAPER_SPECIAL_TOPIC = "wallpaperSpecialTopicGromore"

}

/**
 * 广告类型
 */
object AdShowType {
    const val CUSTOM = "custom"   // 自有
    const val PANGLE = "pangle"   // 穿山甲广告
    const val KUAISHOU = "kuaishou"   // 快手
}