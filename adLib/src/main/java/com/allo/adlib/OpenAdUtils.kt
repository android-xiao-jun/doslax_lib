package com.allo.adlib

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.allo.adlib.TTAdUtils.createClickData
import com.allo.adlib.TTAdUtils.createExposureData
import com.allo.bigData.ReportBigDataHelper
import com.allo.utils.AdUtils
import com.allo.utils.JobScheduler
import com.bytedance.sdk.openadsdk.*

/**
 * Desc 穿山甲广告工具类
 *
 * @author zhangxiaolin
 * Date 2022/4/21
 * Copyright © ALLO
 */
object OpenAdUtils {

    private val ttAdManager: TTAdManager by lazy {
        TTAdSdk.getAdManager()
    }

    /**
     * 初始化穿山甲sdk
     */
    fun initOpenAdSDK(app: Application, appId: String) {
        val openAppId = if (BuildConfig.DEBUG) {
            OPEN_AD_APP_ID
        } else {
            OPEN_AD_APP_ID_RELEASE
        }
        try {
            TTAdSdk.init(
                app, TTAdConfig.Builder()
                    .appId(openAppId)       //这里是测试的appId "5001121"
                    .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                    .appName("ALLO")
                    .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                    .allowShowNotify(true)//是否允许sdk展示通知栏提示
                    .debug(true)//测试阶段打开，可以通过日志排查问题，上线时去除该调用
                    .directDownloadNetworkType(
                        TTAdConstant.NETWORK_STATE_WIFI,
                        TTAdConstant.NETWORK_STATE_3G
                    ) //允许直接下载的网络状态集合
                    .build()
            )
            TTAdSdk.start(object : TTAdSdk.Callback {
                override fun success() {
                    Log.e("===z", "穿山广告 start success.")
                }

                override fun fail(code: Int, msg: String?) {
                    Log.e("===z", "穿山广告失败 code=$code msg=$msg")
                }

            })
        } catch (e: Exception) {
        }
    }

    /**
     * 获取穿山甲激励视频广告
     */
    fun loadOpenRewardVideoAd(
        activity: Activity,
        pageId: String,
        adCode: String? = null,
        listener: ((Boolean) -> Unit),
        showListener: (() -> Unit)
    ) {
        val adSlot = AdSlot.Builder() //此次加载广告的用途是实时加载，当用来作为缓存时，请使用：TTAdLoadType.LOAD
            .setAdLoadType(TTAdLoadType.LOAD)
            .setCodeId(adCode ?: OPEN_AD_CODE_ID_REWARD)
            .build()
        Log.e("===z", "穿山甲激励视频广告id=$adCode")
        TTAdUtils.showLoading(activity)
        //是否完成视频观看
        var finishReward = false
        ttAdManager.createAdNative(activity).loadRewardVideoAd(adSlot, object :
            TTAdNative.RewardVideoAdListener {
            override fun onError(code: Int, msg: String?) {
                Log.e("===z", "穿山甲激励视频广告失败code=$code msg=$msg")
                TTAdUtils.hideLoading()
            }

            override fun onRewardVideoAdLoad(ad: TTRewardVideoAd?) {
                //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
                TTAdUtils.hideLoading()
                ReportBigDataHelper.onExposureStart(
                    createExposureData(pageId, adCode ?: "", AdShowType.PANGLE)
                )
                showRewardVideoAds(ad)
            }

            override fun onRewardVideoCached() {
                TTAdUtils.hideLoading()
            }

            override fun onRewardVideoCached(ad: TTRewardVideoAd?) {
                //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
                TTAdUtils.hideLoading()
                ReportBigDataHelper.onExposureStart(
                    createExposureData(pageId, adCode ?: "", AdShowType.PANGLE)
                )
                //showRewardVideoAds(ad)
            }

            /**
             * 展示激励视频广告
             */
            private fun showRewardVideoAds(ad: TTRewardVideoAd?) {
                ad?.let { ttRewardVideoAd ->
                    //设置监听
                    ttRewardVideoAd.setRewardAdInteractionListener(object :
                        TTRewardVideoAd.RewardAdInteractionListener {
                        override fun onAdShow() {
                            Log.e("===z", "rewardVideoAd show")
                        }

                        override fun onAdVideoBarClick() {
                            Log.e("===z", "rewardVideoAd bar click")
//                            ReportBigDataHelper.onClickEvent(
//                                createClickData(pageId, adCode ?: "", AdShowType.PANGLE)
//                            )
                        }

                        override fun onAdClose() {
                            Log.e("===z", "rewardVideoAd close")
                            callBackResult(msg = "onAdClose")
//                            ReportBigDataHelper.onExposureEnd(
//                                createExposureData(pageId, adCode ?: "", AdShowType.PANGLE)
//                            )
//                            ReportBigDataHelper.uploadExposureData(pageId)
                        }

                        override fun onVideoComplete() {
                            Log.e("===z", "rewardVideoAd complete")
                            /*callBackResult(msg = "onVideoComplete")*/
                        }

                        override fun onVideoError() {
                            callBackResult(msg = "onVideoError")
                            Log.e("===z", "rewardVideoAd error")
                        }

                        /**
                         * 视频播放完成后，奖励验证回调
                         * @param rewardVerify   是否有效
                         * @param rewardAmount   奖励梳理
                         * @param rewardName     奖励名称
                         * @param errorCode
                         * @param errorMsg
                         */
                        override fun onRewardVerify(
                            rewardVerify: Boolean,
                            rewardAmount: Int,
                            rewardName: String?,
                            errorCode: Int,
                            errorMsg: String?
                        ) {
                            val logString = "verify:" + rewardVerify + " amount:" + rewardAmount +
                                    " name:" + rewardName + " errorCode:" + errorCode + " errorMsg:" + errorMsg
                            finishReward = true
                        }

                        override fun onRewardArrived(
                            isRewardValid: Boolean,
                            rewardType: Int,
                            extraInfo: Bundle?
                        ) {
                            //奖励发放以这个参数为准
                            finishReward = isRewardValid
                        }

                        override fun onSkippedVideo() {
                            //这里跳过不能设置成false 穿山甲的奖励可能不需要看完视频就到账
                            //获取奖励可能在跳过之前
                            callBackResult(msg = "onSkippedVideo")
                        }

                    })
                    //展示广告
                    ttRewardVideoAd.showRewardVideoAd(
                        activity,
                        TTAdConstant.RitScenes.CUSTOMIZE_SCENES,
                        "scenes_test"
                    )
                }
                showListener.invoke()
            }

            private fun callBackResult(msg: String? = null) {
                JobScheduler.uiJob {
                    listener.invoke(finishReward)
                    Log.e("===z", "callBackResult回调 finishReward=$finishReward msg=$msg")
                    ReportBigDataHelper.onExposureEnd(
                        createExposureData(pageId, adCode ?: "", AdShowType.PANGLE)
                    )
                    ReportBigDataHelper.uploadExposureData(pageId)
                }
            }
        })
    }

    /**
     * 加载穿山甲开屏广告
     */
    fun loadOpenSplashScreenAd(
        splashAdContainer: FrameLayout,
        adCode: String? = null,
        context: Activity,
        adLoadListener: TTAdUtils.OnSplashAdLoadListener
    ) {
        Log.e("===z", "穿山甲开屏广告loadOpenSplashScreenAd")
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        val dm = DisplayMetrics()
        context.windowManager.defaultDisplay.getRealMetrics(dm)
        val splashWidthDp: Float = AdUtils.getScreenWidthDp(context)
        val splashWidthPx: Int = AdUtils.getScreenWidthInPx(context)
        val screenHeightPx: Int = AdUtils.getScreenHeight(context)
        val screenHeightDp: Int = AdUtils.px2dip(context, screenHeightPx.toFloat())
        val splashHeightDp: Float = screenHeightDp.toFloat()
        val splashHeightPx: Int = screenHeightPx
        val adSlot = AdSlot.Builder()
            .setCodeId(adCode ?: OPEN_AD_CODE_ID_SPLASHSCREEN)
            //模板广告需要设置期望个性化模板广告的大小,单位dp,代码位是否属于个性化模板广告，请在穿山甲平台查看
            //view宽高等于图片的宽高
            .setExpressViewAcceptedSize(splashWidthDp, splashHeightDp) // 单位是dp
            .setImageAcceptedSize(splashWidthPx, splashHeightPx) // 单位是px
            .setAdCount(1)
            .build()
        ttAdManager.createAdNative(context).loadSplashAd(adSlot, object :
            TTAdNative.SplashAdListener {
            override fun onError(code: Int, msg: String?) {
                Log.e("===z", "开屏广告加载失败code=$code msg=$msg")
                adLoadListener.onError("code=$code msg=$msg")
            }

            override fun onTimeout() {
                Log.e("===z", "获取穿山甲开屏广告超时")
                adLoadListener.onError("获取穿山甲开屏广告超时")
            }

            override fun onSplashAdLoad(ad: TTSplashAd?) {
                Log.e("===z", "穿山甲开屏广告onSplashAdLoad")
                adLoadListener.onAdSuccess()
                addView(ad)
            }

            private fun addView(ad: TTSplashAd?) {
                ad?.let { ttSplashAd ->
                    //设置监听
                    ttSplashAd.setSplashInteractionListener(object :
                        TTSplashAd.AdInteractionListener {
                        override fun onAdClicked(view: View?, type: Int) {
                            adLoadListener.onAdClicked()
                            Log.e("===z", "穿山甲开屏广告onAdClicked")
                        }

                        override fun onAdShow(view: View?, type: Int) {
                            adLoadListener.onAdShowStart()
                            Log.e("===z", "穿山甲开屏广告onAdShowStart")
                        }

                        override fun onAdSkip() {
                            Log.e("===z", "穿山甲开屏广告onAdSkip")
                            adLoadListener.onSkippedAd()
                        }

                        override fun onAdTimeOver() {
                            Log.e("===z", "穿山甲开屏广告onAdTimeOver")
                            adLoadListener.onAdShowEnd()
                        }

                    })
                    //将广告添加到容器
                    val view = ttSplashAd.splashView
                    if (!context.isFinishing) {
                        splashAdContainer.removeAllViews()
                        view.layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        splashAdContainer.addView(view)
                        Log.e("===z", "添加穿山甲开屏广告")
                    }
                } ?: let {
                    Log.e("===z", "获取穿山甲开屏广告数据为空")
                    adLoadListener.onError("获取穿山甲开屏广告数据为空")
                }
            }
        })
    }

    /**
     * 加载穿山甲插屏广告(旧插屏)
     * 穿山甲已经不支持新建旧插屏了
     */
    fun loadOpenInteractionAd(
        activity: Activity,
        adContainer: ViewGroup,
        adCode: String? = null,
        pageId: String? = null,
    ) {
        val adSlot = AdSlot.Builder()
            .setCodeId(adCode ?: "")
            .setSupportDeepLink(true)
            .setAdCount(1) //请求广告数量为1到3条
            .setExpressViewAcceptedSize(240f, 240f) //期望模板广告view的size,单位dp
            .build()
        ttAdManager.createAdNative(activity)
            .loadInteractionExpressAd(adSlot, object : TTAdNative.NativeExpressAdListener {
                override fun onError(code: Int, msg: String?) {
                    //加载插屏广告失败
                }

                override fun onNativeExpressAdLoad(ad: MutableList<TTNativeExpressAd>?) {
                    //加载插屏广告成功
                    ad?.let {
                        if (it.size == 0) {
                            return@let
                        }
                        val ttNativeExpressAd = it[0]
                        ttNativeExpressAd.setExpressInteractionListener(object :
                            TTNativeExpressAd.ExpressAdInteractionListener {
                            override fun onAdClicked(p0: View?, p1: Int) {

                            }

                            override fun onAdShow(p0: View?, p1: Int) {
                            }

                            override fun onRenderFail(p0: View?, p1: String?, p2: Int) {
                            }

                            override fun onRenderSuccess(p0: View?, p1: Float, p2: Float) {
                            }

                        })
                        //将广告添加到容器
                        adContainer.removeAllViews()
                        ttNativeExpressAd.expressAdView.layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        if (ttNativeExpressAd.expressAdView.parent != null) {
                            (ttNativeExpressAd.expressAdView.parent as ViewGroup).removeAllViews()
                        }
                        adContainer.addView(ttNativeExpressAd.expressAdView)
                        ttNativeExpressAd.render()

                    }
                }

            })
    }

    /**
     * 加载穿山甲插屏广告(新插屏)
     */
    fun loadOpenFullScreenVideoAd(
        adCode: String? = null,
        activity: Activity,
        listener: TTAdUtils.OnAdLoadListener? = null,
    ) {
        val adSlot = AdSlot.Builder() //此次加载广告的用途是实时加载，当用来作为缓存时，请使用：TTAdLoadType.LOAD
            .setAdLoadType(TTAdLoadType.LOAD)
            .setCodeId(adCode ?: OPEN_AD_CODE_ID_FULL_SCREEN_VIDEO)
            .build()
        ttAdManager.createAdNative(activity).loadFullScreenVideoAd(adSlot, object :
            TTAdNative.FullScreenVideoAdListener {
            override fun onError(code: Int, msg: String?) {
            }

            override fun onFullScreenVideoAdLoad(ad: TTFullScreenVideoAd?) {
                ad?.let { ttFullScreenVideoAd ->
                    //设置监听
                    ttFullScreenVideoAd.setFullScreenVideoAdInteractionListener(object :
                        TTFullScreenVideoAd.FullScreenVideoAdInteractionListener {
                        override fun onAdShow() {
                            //广告曝光
                            listener?.onAdShow()
                        }

                        override fun onAdVideoBarClick() {
                            listener?.onAdClick()
                        }

                        override fun onAdClose() {
                            listener?.onAdClose()
                        }

                        override fun onVideoComplete() {
                        }

                        override fun onSkippedVideo() {
                        }

                    })
                    //展示广告
                    if (!activity.isFinishing) {
                        ttFullScreenVideoAd.showFullScreenVideoAd(
                            activity,
                            TTAdConstant.RitScenes.GAME_GIFT_BONUS, null
                        )
                    }
                }
            }

            override fun onFullScreenVideoCached() {
            }

            override fun onFullScreenVideoCached(ad: TTFullScreenVideoAd?) {
            }

        })
    }


    /**
     * 加载穿山甲图文广告(左图右文)
     * 模板渲染穿山甲广告
     */
    fun loadOpenExpressAd(
        activity: Activity,
        adCode: String? = null,
        isNeedWidth: Boolean? = null,
        adContainer: ViewGroup,
        listener: TTAdUtils.OnAdLoadListener,
        width: Float? = null,
    ) {
        Log.e("===z", "模板渲染穿山甲广告 adCode=$adCode")
        val expressViewWidth = if (isNeedWidth == true) {
            width ?: 180f
        } else {
            350f
        }
        //高度设置为0,则高度会自适应
        val expressViewHeight = 0f
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        val adSlot = AdSlot.Builder()
            .setCodeId(adCode ?: OPEN_AD_CODE_ID_EXPRESS) //广告位id
            .setAdCount(3) //请求广告数量为1到3条
            .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
            .build()
        ttAdManager.createAdNative(activity).loadNativeExpressAd(adSlot, object :
            TTAdNative.NativeExpressAdListener {
            override fun onError(code: Int, msg: String?) {
                Log.e("===z", "请求广告失败code=$code msg=$msg")
                listener.onAdLoadFailed("请求广告失败code=$code msg=$msg")
            }

            override fun onNativeExpressAdLoad(ad: MutableList<TTNativeExpressAd>?) {
                Log.e("===z", "请求广告onNativeExpressAdLoad ad=$ad")
                ad?.let { list ->
                    if (list.size == 0) {
                        return@let
                    }
                    listener.onAdLoadSuccess(null)

                    val ttNativeExpressAd = list[0]
                    ttNativeExpressAd.setExpressInteractionListener(object :
                        TTNativeExpressAd.ExpressAdInteractionListener {
                        override fun onAdClicked(p0: View?, p1: Int) {
                            Log.e("===z", "广告点击")
                            listener.onAdClick()
                        }

                        override fun onAdShow(p0: View?, p1: Int) {
                            Log.e("===z", "广告展示")
                            listener.onAdShow()
                        }

                        override fun onRenderFail(view: View?, msg: String?, code: Int) {
                            //渲染失败
                            Log.e("===z", "广告渲染失败")
                            listener.onAdLoadFailed("渲染失败code=$code msg=$msg")
                        }

                        override fun onRenderSuccess(view: View?, width: Float, height: Float) {
                            //渲染成功 返回view的宽高 单位 dp
                            view?.let { adView ->
                                adContainer.removeAllViews()
                                adView.layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                if (adView.parent != null) {
                                    (adView.parent as ViewGroup).removeAllViews()
                                }
                                adContainer.addView(adView)
                                adView.post {
                                    Log.e(
                                        "===z",
                                        "广告View adContainer=${adContainer.width}  adView=${adView.width}"
                                    )
                                }
                            } ?: let {
                                //广告View为空
                                Log.e("===z", "广告View为空")
                                listener.onAdLoadFailed("广告View为空")
                            }
                        }
                    })
                    //关闭设置
                    ttNativeExpressAd.setDislikeCallback(activity, object :
                        TTAdDislike.DislikeInteractionCallback {
                        override fun onShow() {
                        }

                        override fun onSelected(position: Int, value: String?, enforce: Boolean) {
                            //用户选择不喜欢原因后，移除广告展示
                            adContainer.removeAllViews()
                            listener.onAdClose()
                        }

                        override fun onCancel() {
                        }

                    })
                    //渲染
                    ttNativeExpressAd.render()
                } ?: let {
                    //广告数据为空
                    Log.e("===z", "广告数据为空")
                    listener.onAdLoadFailed("广告数据为空")
                }
            }

        })
    }


}

//穿山甲广告appId 正式应用：5191255  测试应用  5185877
const val OPEN_AD_APP_ID = "5185877"

//正式应用appId
const val OPEN_AD_APP_ID_RELEASE = "5191255"

//穿山甲 广告位id 激励视频
const val OPEN_AD_CODE_ID_REWARD = "901121365"

//穿山甲半屏开屏广告位id
const val OPEN_AD_CODE_ID_SPLASHSCREEN = "801121648"

//穿山甲新版竖屏插屏广告
const val OPEN_AD_CODE_ID_FULL_SCREEN_VIDEO = "947793385"

//穿山甲图文广告位id (铃声列表)
const val OPEN_AD_CODE_ID_EXPRESS = "901121253"
