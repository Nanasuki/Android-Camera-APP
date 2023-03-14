package com.clothes.login.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.clothes.login.global.AppConstants;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;


public class ShareUtils {
	// 整个平台的Controller,负责管理整个SDK的配置、操作等处理
	private static UMSocialService mController = UMServiceFactory
			.getUMSocialService(AppConstants.DESCRIPTOR);


	// 显示分享平台
	public static void showShareBoard(Context context) {
		mController.getConfig().setPlatforms(SHARE_MEDIA.SINA,
				SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.WEIXIN);
		registerCallback(context);
		mController.openShare((Activity) context, false);

	}

	public static void showShareBoard(final Context context,
			final String link_id, final String hash) {
		mController.getConfig().setPlatforms(SHARE_MEDIA.SINA,
				SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.WEIXIN);
		mController.getConfig().cleanListeners();
		mController.getConfig().registerListener(new SnsPostListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int stCode,
					SocializeEntity entity) {
				if (stCode == 200) {
					// TODO: 这里可以进行分享数统计
				} else {
//					Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();

				}
			}
		});
		mController.openShare((Activity) context, false);

	}

	/**
	 * 设置分享回调
	 * 
	 * @param
	 */
	public static void registerCallback(final Context context) {
		mController.getConfig().cleanListeners();
		mController.getConfig().registerListener(new SnsPostListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int stCode,
					SocializeEntity entity) {
				if (stCode == 200) {
					Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
