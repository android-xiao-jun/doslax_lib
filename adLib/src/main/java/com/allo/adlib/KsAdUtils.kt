package com.allo.adlib

/**
 * Desc 快手广告工具类
 *
 * @author zhangxiaolin
 * Date 2022/4/21
 * Copyright © ALLO
 */
object KsAdUtils {
//
//    /**
//     * 初始化快手sdk
//     */
//    fun initKsAdSDK(app: Application) {
//        val appId = if (BuildConfig.DEBUG) {
//            KS_AD_APP_ID
//        } else {
//            KS_AD_APP_ID_RELEASE
//        }
//        KsAdSDK.init(
//            app, SdkConfig.Builder()
//                .appId(appId) // 测试aapId，请联系快手平台申请正式AppId，必填
//                .appName("ALLO") // 测试appName，请填写您应用的名称，非必填
//                // Feed和入口组件，夜间模式样式配置，如果不配置 默认是"ks_adsdk_night_styles.xml"
//                .nightThemeStyleAssetsFileName("ks_adsdk_night_styles.xml")
//                .showNotification(true) // 是否展示下载通知栏
//                .debug(false)
//                .setInitCallback(object : KsInitCallback {
//                    override fun onSuccess() {
//                        Log.e("===z", "ks sdk init success")
//                    }
//
//                    override fun onFail(code: Int, msg: String) {
//                        Log.e("===z", "ks sdk init fail code:$code--msg:$msg")
//                    }
//                })
//                .build()
//        )
//    }
//
//    /**
//     * 获取快手开屏广告
//     */
//    fun loadKsSplashScreenAd(
//        splashAdContainer: FrameLayout,
//        adCode: String? = null,
//        context: Activity,
//        adLoadListener: TTAdUtils.OnSplashAdLoadListener
//    ) {
//        val scene: KsScene =
//            KsScene.Builder(
//                parsePosId(
//                    adCode,
//                    KS_AD_POSID_SPLASHSCREEN
//                )
//            ) // 此为测试posId，请联系快手平台申请正式posId
//                .adNum(1)
//                .build()
//        KsAdSDK.getLoadManager()
//            ?.loadSplashScreenAd(scene,
//                object : KsLoadManager.SplashScreenAdListener {
//                    override fun onError(code: Int, msg: String?) {
//                        //请求快手开屏广告失败 到主页
//                        adLoadListener.onError("code=$code msg=$msg")
//                        Log.e("===z", "快手开屏广告失败code=$code msg=$msg")
//                    }
//
//                    override fun onRequestResult(adNumber: Int) {
//                        //
//                        Log.e("===z", "开屏广告填充：$adNumber")
//                    }
//
//                    override fun onSplashScreenAdLoad(splashScreenAd: KsSplashScreenAd?) {
//                        //请求快手开屏广告成功开始返回数据
//                        adLoadListener.onAdSuccess()
//                        addView(splashScreenAd)
//                    }
//
//                    private fun addView(ksSplashScreenAd: KsSplashScreenAd?) {
//                        val adView: View? = ksSplashScreenAd?.getView(context, object :
//                            KsSplashScreenAd.SplashScreenAdInteractionListener {
//                            override fun onAdClicked() {
//                                //开屏广告点击
//                                Log.e("===z", "开屏广告点击")
//                                adLoadListener.onAdClicked()
//                            }
//
//                            override fun onAdShowError(code: Int, extra: String?) {
//                                Log.e("===z", "开屏广告显示错误 $code -- extra $extra")
//                                adLoadListener.onError("开屏广告显示错误code=$code msg=$extra")
//                            }
//
//                            override fun onAdShowEnd() {
//                                //开屏广告显示结束
//                                Log.e("===z", "开屏广告显示结束")
//                                //这里不回调onAdShowEnd 不知怎么的 手动跳过
//                                adLoadListener.onAdShowEnd()
//                            }
//
//                            override fun onAdShowStart() {
//                                //开屏广告显示开始
//                                // 不知怎么的 这里死活不回调onAdShowEnd 手动跳过
//                                Log.e("===z", "开屏广告显示开始")
//                                adLoadListener.onAdShowStart()
//                            }
//
//                            override fun onSkippedAd() {
//                                //用户跳过开屏广告
//                                Log.e("===z", "用户跳过开屏广告")
//                                adLoadListener.onSkippedAd()
//                            }
//
//                            override fun onDownloadTipsDialogShow() {
//                                //开屏广告显示下载合规弹窗
//                                Log.e("===z", "开屏广告显示下载合规弹窗")
//                            }
//
//                            override fun onDownloadTipsDialogDismiss() {
//                                //开屏广告关闭下载合规弹窗
//                                Log.e("===z", "开屏广告关闭下载合规弹窗")
//                            }
//
//                            override fun onDownloadTipsDialogCancel() {
//                                //开屏广告取消下载合规弹窗
//                                Log.e("===z", "开屏广告取消下载合规弹窗")
//                            }
//
//                        })
//                        //将广告添加到容器
//                        adView?.let { view ->
//                            if (!context.isFinishing) {
//                                splashAdContainer.removeAllViews()
//                                view.layoutParams = ViewGroup.LayoutParams(
//                                    ViewGroup.LayoutParams.MATCH_PARENT,
//                                    ViewGroup.LayoutParams.MATCH_PARENT
//                                )
//                                splashAdContainer.addView(view)
//                            }
//                        } ?: let {
//                            adLoadListener.onError("adView为空")
//                        }
//                    }
//
//                }) ?: let {
//            //初始化快手广告失败
//            adLoadListener.onError("初始化快手广告失败")
//        }
//
//    }
//
//    /**
//     * 获取快手Draw信息流广告 预览页上下滑动的广告
//     */
//    fun loadKsDrawAd(
//        activity: Activity,
//        adCode: String? = null,
//        flItemAdContainer: FrameLayout,
//        listener: TTAdUtils.OnDrawAdLoadListener
//    ) {
//        val scene: KsScene =
//            KsScene.Builder(
//                parsePosId(
//                    adCode,
//                    KS_AD_POSID_DRAW
//                )
//            ) // 此为测试posId，请联系快手平台申请正式posId
//                .adNum(1)// 支持返回多条广告，默认1条，最多5条，参数范围1-5
//                .build()
//        KsAdSDK.getLoadManager()?.loadDrawAd(scene, object :
//            KsLoadManager.DrawAdListener {
//            override fun onError(code: Int, msg: String?) {
//                //广告数据请求失败
//                Log.e("===z", "广告数据请求失败code=$code msg=$msg")
//                listener.onError("广告数据请求失败code=$code msg=$msg")
//            }
//
//            override fun onDrawAdLoad(adList: MutableList<KsDrawAd>?) {
//                adList?.let { list ->
//                    if (list.size == 0) {
//                        return@let
//                    }
//                    val ksDrawAd = list[0]
//                    //设置回调
//                    ksDrawAd.setAdInteractionListener(object :
//                        KsDrawAd.AdInteractionListener {
//                        override fun onAdClicked() {
//                            //广告点击回调
//                            listener.onAdClicked()
//                        }
//
//                        override fun onAdShow() {
//                            //广告曝光回调
//                            listener.onAdShow()
//                        }
//
//                        override fun onVideoPlayStart() {
//                            //广告视频开始播放
//                            listener.onVideoPlayStart()
//                        }
//
//                        override fun onVideoPlayPause() {
//                            //广告视频暂停播放
//                            listener.onVideoPlayPause()
//                        }
//
//                        override fun onVideoPlayResume() {
//                            //广告视频恢复播放
//                            listener.onVideoPlayResume()
//                        }
//
//                        override fun onVideoPlayEnd() {
//                            //广告视频播放结束
//                            listener.onVideoPlayEnd()
//                        }
//
//                        override fun onVideoPlayError() {
//                            //广告视频播放出错
//                            listener.onVideoPlayError()
//                        }
//
//                    })
//                    //添加广告View到容器
//                    val drawView = ksDrawAd.getDrawView(activity)
//                    drawView?.let { adView ->
//                        flItemAdContainer.removeAllViews()
//                        if (adView.parent != null) {
//                            (adView.parent as ViewGroup).removeAllViews()
//                        }
//                        flItemAdContainer.addView(adView)
//                    } ?: let {
//                        listener.onError("Draw信息流广告View为空")
//                    }
//                }
//            }
//
//        }) ?: let {
//            //Draw信息流广告初始化失败
//            listener.onError("Draw信息流广告初始化失败")
//        }
//
//    }
//
//    /**
//     * 获取快手原生广告 --铃声列表
//     * 模板渲染
//     */
//    fun loadKsConfigFeedAd(
//        activity: Activity,
//        adPosId: String? = null,
//        adContainer: ViewGroup,
//        listener: TTAdUtils.OnAdLoadListener,
//        isNeedWidth: Boolean? = null,
//        width: Float? = null,
//    ) {
//        val posId: Long = parsePosId(adPosId, KS_AD_POSID_CONFIG_FEED)
//        val scene = if (isNeedWidth == true) {
//            val adWidth = width?.toInt() ?: 350
//            KsScene.Builder(posId).adNum(1).width(adWidth).build()
//        } else {
//            KsScene.Builder(posId).adNum(1).build()
//        }
//        KsAdSDK.getLoadManager()?.loadConfigFeedAd(scene, object :
//            KsLoadManager.FeedAdListener {
//            override fun onError(code: Int, msg: String?) {
//                Log.e("===z", "广告数据请求失败code=$code msg=$msg")
//                listener.onAdLoadFailed("广告数据请求失败code=$code msg=$msg")
//            }
//
//            override fun onFeedAdLoad(adList: MutableList<KsFeedAd>?) {
//                adList?.let { list ->
//                    if (list.size == 0) {
//                        return@let
//                    }
//                    listener.onAdLoadSuccess(null)
//                    val ksFeedAd = list[0]
//                    //设置监听回调
//                    ksFeedAd.setAdInteractionListener(object :
//                        KsFeedAd.AdInteractionListener {
//                        override fun onAdClicked() {
//                            //广告点击回调
//                            listener.onAdClick()
//                        }
//
//                        override fun onAdShow() {
//                            //广告曝光回调
//                            listener.onAdShow()
//                        }
//
//                        override fun onDislikeClicked() {
//                            //广告不喜欢回调
//                            listener.onAdClose()
//                            adContainer.removeAllViews()
//                        }
//
//                        override fun onDownloadTipsDialogShow() {
//                            //广告展示下载合规弹窗
//                        }
//
//                        override fun onDownloadTipsDialogDismiss() {
//                            //广告关闭下载合规弹窗
//                        }
//
//                    })
//                    //将广告View添加到容器
//                    val feedView = ksFeedAd.getFeedView(activity)
//                    feedView?.let { view ->
//                        adContainer.removeAllViews()
//                        view.layoutParams = ViewGroup.LayoutParams(
//                            ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.MATCH_PARENT
//                        )
//                        if (view.parent != null) {
//                            (view.parent as ViewGroup).removeAllViews()
//                        }
//                        adContainer.addView(view)
//                    } ?: let {
//                        //广告View为空
//                        listener.onAdLoadFailed("广告View为空")
//                    }
//                } ?: let {
//                    //广告数据为空
//                    listener.onAdLoadFailed("广告数据为空")
//                }
//            }
//        }) ?: let {
//            //广告初始化失败
//            listener.onAdLoadFailed("广告初始化失败")
//        }
//    }
//
//    fun parsePosId(adPosId: String?, defaultPosId: Long): Long {
//        val posId: Long?
//        if (adPosId.isNullOrEmpty()) {
//            posId = defaultPosId
//        } else {
//            posId = adPosId.toLong()
//        }
//        Log.e("===z", "posId=$posId")
//        return posId
//        /*return if (adPosId.isNullOrEmpty()) {
//            defaultPosId
//        } else {
//            adPosId.toLong()
//        }*/
//    }
//
//    /**
//     * 加载快手原生广告 --首页视频和壁纸
//     * 广告选择 自渲染 竖屏图片 才能正常适配
//     */
//    fun loadKsNativeAd(adPosId: String? = null, listener: TTAdUtils.OnHomeNativeKsAdLoadListener) {
//        val scene = KsScene.Builder(parsePosId(adPosId, KS_AD_POSID_NATIVE_IMAGE)).adNum(1).build()
//        KsAdSDK.getLoadManager()?.loadNativeAd(scene, object :
//            KsLoadManager.NativeAdListener {
//            override fun onError(code: Int, msg: String?) {
//                Log.e("===z", "广告数据请求失败$code $msg")
//                listener.onError("广告数据请求失败$code $msg")
//            }
//
//            override fun onNativeAdLoad(adList: MutableList<KsNativeAd>?) {
//                adList?.let { list ->
//                    listener.onNativeAdLoad(list[0])
//                } ?: let {
//                    //广告数据为空
//                    listener.onError("广告数据为空")
//                }
//            }
//
//        }) ?: let {
//            //广告初始化失败
//            listener.onError("广告初始化失败")
//        }
//    }
//
//    /**
//     * 获取插屏广告
//     */
//    fun loadKsInterstitialAd(
//        adCode: String? = null, activity: Activity, listener: TTAdUtils.OnAdLoadListener? = null,
//    ) {
//        val scene =
//            KsScene.Builder(parsePosId(adCode, KS_AD_POSID_INTERSTITIAL))
//                .build()
//        KsAdSDK.getLoadManager()?.loadInterstitialAd(scene,
//            object : KsLoadManager.InterstitialAdListener {
//                override fun onError(code: Int, msg: String?) {
//                    Log.e("===z", "插屏广告onError code=$code msg=$msg")
//                }
//
//                override fun onRequestResult(adNumber: Int) {
//                    Log.e("===z", "插屏广告请求填充个数 $adNumber")
//                }
//
//                override fun onInterstitialAdLoad(adList: MutableList<KsInterstitialAd>?) {
//                    adList?.let { list ->
//                        val ksInterstitialAd = list[0]
//                        val videoPlayConfig = KsVideoPlayConfig.Builder()
//                            .showLandscape(false)
//                            .videoSoundEnable(false)
//                            .build()
//                        //设置监听
//                        ksInterstitialAd.setAdInteractionListener(object :
//                            KsInterstitialAd.AdInteractionListener {
//                            override fun onAdClicked() {
//                                listener?.onAdClick()
//                            }
//
//                            override fun onAdShow() {
//                                Log.e("===z", "插屏展示")
//                                listener?.onAdShow()
//                            }
//
//                            override fun onAdClosed() {
//                                listener?.onAdClose()
//                            }
//
//                            override fun onPageDismiss() {
//                                listener?.onAdClose()
//                            }
//
//                            override fun onVideoPlayError(p0: Int, p1: Int) {
//                            }
//
//                            override fun onVideoPlayEnd() {
//                            }
//
//                            override fun onVideoPlayStart() {
//                            }
//
//                            override fun onSkippedAd() {
//                            }
//
//                        })
//                        if (!activity.isFinishing) {
//                            ksInterstitialAd.showInterstitialAd(activity, videoPlayConfig)
//                        }
//                    }
//                }
//            })
//    }
//
//    /**
//     * 加载快手激励视频广告
//     */
//    fun loadKsRewardVideoAd(
//        activity: Activity,
//        pageId: String,
//        adPosId: String? = null,
//        listener: ((Boolean) -> Unit),
//        showListener: (() -> Unit)
//    ) {
//        TTAdUtils.showLoading(activity)
//        //是否完成视频观看
//        var finishReward = false
//        //快手激励视频
//        val posId: Long = parsePosId(adPosId, KS_AD_POSID_REWARD)
//        val scene = KsScene.Builder(posId) // posId 为平台申请⼴告位id 必填
//            .build()
//        KsAdSDK.getLoadManager()?.loadRewardVideoAd(scene, object :
//            KsLoadManager.RewardVideoAdListener {
//            override fun onError(code: Int, msg: String?) {
//                //快手激励视频广告请求失败
//                TTAdUtils.hideLoading()
//                finishReward = false
//                Log.e("===z", "快手激励视频广告请求失败 code=$code msg=$msg")
//            }
//
//            override fun onRewardVideoResult(adList: MutableList<KsRewardVideoAd>?) {
//                //视频广告的数据加载完毕，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
//                TTAdUtils.hideLoading()
//                Log.e("===z", "快手激励视频广告请求成功 adList=$adList")
//                ReportBigDataHelper.onExposureStart(
//                    TTAdUtils.createExposureData(pageId, adPosId ?: "", AdShowType.KUAISHOU)
//                )
//                showRewardVideoAd(adList)
//            }
//
//            override fun onRewardVideoAdLoad(adList: MutableList<KsRewardVideoAd>?) {
//                //视频广告的数据加载和资源缓存完毕，在此回调后，播放本地视频，流畅不阻塞。
//                TTAdUtils.hideLoading()
//                Log.e("===z", "快手激励视频⼴告数据请求且资源缓存成功 adList=$adList")
//
//                //showRewardVideoAd(adList)
//            }
//
//            //2.展示激励视频，可以竖屏展示也可以横屏展示，建议与当前屏幕方向一致
//            private fun showRewardVideoAd(adList: MutableList<KsRewardVideoAd>?) {
//                ReportBigDataHelper.onExposureStart(
//                    TTAdUtils.createExposureData(pageId, adPosId ?: "", AdShowType.KUAISHOU)
//                )
//                adList?.let {
//                    if (adList.size == 0) {
//                        return@let
//                    }
//                    val rewardVideoAd: KsRewardVideoAd = adList[0]
//                    //1.设置监听
//                    rewardVideoAd.setInnerAdInteractionListener(object :
//                        KsInnerAd.KsInnerAdInteractionListener {
//                        override fun onAdClicked(ksInnerAd: KsInnerAd?) {
//                            Log.e("===z", "激励视频内部广告点击：" + ksInnerAd?.type)
//                        }
//
//                        override fun onAdShow(ksInnerAd: KsInnerAd?) {
//                            Log.e("===z", "激励视频内部广告曝光：" + ksInnerAd?.type)
//
//                        }
//                    })
//                    rewardVideoAd.setRewardAdInteractionListener(object :
//                        KsRewardVideoAd.RewardAdInteractionListener {
//                        override fun onAdClicked() {
//                            Log.e("===z", "激励视频广告点击")
//                            // 点击广告上报
//                            ReportBigDataHelper.onClickEvent(
//                                createClickData(pageId, adPosId ?: "", AdShowType.KUAISHOU)
//                            )
//                        }
//
//                        override fun onPageDismiss() {
//                            //无论如何最后都会执行这个方法
//                            Log.e("===z", "激励视频广告关闭")
//                            callBackResult()
//                        }
//
//                        override fun onVideoPlayError(code: Int, extra: Int) {
//                            Log.e("===z", "激励视频广告播放出错")
//                            callBackResult()
//                        }
//
//                        override fun onVideoPlayEnd() {
//                            Log.e("===z", "激励视频广告播放完成")
//                            finishReward = true
//                        }
//
//                        override fun onVideoSkipToEnd(playDuration: Long) {
//                            Log.e("===z", "激励视频广告跳过播放完成")
//                            //没看完关闭会执行onVideoSkipToEnd
//                            //finishReward = false
////                            callBackResult()
//                        }
//
//                        override fun onVideoPlayStart() {
//                            Log.e("===z", "激励视频广告播放开始")
//                        }
//
//                        /**
//                         * 激励视频广告激励回调，只会回调一次
//                         */
//                        override fun onRewardVerify() {
//                            Log.e("===z", "激励视频广告获取激励")
//                            finishReward = true
//                        }
//
//                        /**
//                         *  视频激励分阶段回调
//                         * @param taskType 当前激励视频所属任务类型
//                         *                 RewardTaskType.LOOK_VIDEO 观看视频类型             属于浅度奖励类型
//                         *                 RewardTaskType.LOOK_LANDING_PAGE 浏览落地页N秒类型  属于深度奖励类型
//                         *                 RewardTaskType.USE_APP 下载使用App N秒类型          属于深度奖励类型
//                         * @param currentTaskStatus  当前所完成任务类型，@RewardTaskType中之一
//                         */
//                        override fun onRewardStepVerify(taskType: Int, currentTaskStatus: Int) {
//                            finishReward = true
//                        }
//
//                        override fun onExtraRewardVerify(p0: Int) {
//                        }
//
//                    })
//
//                    //2.设置展示配置
//                    val videoPlayConfig = KsVideoPlayConfig.Builder()
//                        .build()
//                    //3.展示激励视频广告
//                    rewardVideoAd.showRewardVideoAd(activity, videoPlayConfig)
//                }
//                showListener.invoke()
//            }
//
//            private fun callBackResult() {
//                JobScheduler.uiJob {
//                    listener.invoke(finishReward)
//                    Log.e("===z","回调invoke")
//                    ReportBigDataHelper.onExposureEnd(
//                        TTAdUtils.createExposureData(pageId, adPosId ?: "", AdShowType.KUAISHOU)
//                    )
//                    ReportBigDataHelper.uploadExposureData(pageId)
//                }
//            }
//
//        })
//    }

}

/**-------测试---------*/
//快手广告appId
const val KS_AD_APP_ID = "906000002" //测试应用

const val KS_AD_APP_ID_RELEASE = "906000001" //正式应用

//广告位id 激励视频
const val KS_AD_POSID_REWARD = 90009001L

//快手开屏广告测试PosId
const val KS_AD_POSID_SPLASHSCREEN = 4000000042L

//快手Draw信息流测试PosId
const val KS_AD_POSID_DRAW = 4000000020L

//快手Feed测试+文字悬浮在图片
const val KS_AD_POSID_CONFIG_FEED = 4000000001L

//快手图文测试PosId(首页视频和壁纸广告)
const val KS_AD_POSID_NATIVE_IMAGE = 90009004L

//快手插屏视频测试PosId
const val KS_AD_POSID_INTERSTITIAL = 4000000276L