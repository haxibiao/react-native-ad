/*
 * @Author: Bin
 * @Date: 2022-02-06
 * @FilePath: /react-native-ad/example/src/Splash.tsx
 */
import React, {useEffect} from 'react';
import {View, Text, TouchableOpacity, StyleSheet} from 'react-native';

import {ad} from 'react-native-ad';

export default function Splash() {
  useEffect(() => {
    ad.init({
      appid: '5016582',
      codeid_splash: '816582039',
    });
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.welcome}>
        ☆ Splash Screen example Powered By HaXiBiao ☆
      </Text>
      <TouchableOpacity
        style={{
          marginVertical: 20,
          paddingHorizontal: 30,
          paddingVertical: 15,
          backgroundColor: '#F96',
          borderRadius: 50,
        }}
        onPress={() => {
          const splashAd = ad.startSplash({
            appid: '5016582',
            codeid: '816582039',
            provider: '头条',
            anim: 'default',
          });

          splashAd.subscribe('onAdClose', event => {
            console.log('广告关闭', event);
          });

          splashAd.subscribe('onAdSkip', i => {
            console.log('用户点击跳过监听', i);
          });

          splashAd.subscribe('onAdError', e => {
            console.log('开屏加载失败监听', e);
          });

          splashAd.subscribe('onAdClick', e => {
            console.log('开屏被用户点击了', e);
          });

          splashAd.subscribe('onAdShow', e => {
            console.log('开屏开始展示', e);
          });
        }}>
        <Text style={{textAlign: 'center'}}> Start SplashAd</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
