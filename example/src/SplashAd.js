import React from 'react';
import {StyleSheet, Text, View, TouchableOpacity} from 'react-native';
import {ad} from 'react-native-ad';

export default function SplashAd() {
  return (
    <View style={styles.container}>
      <Text style={styles.welcome}>
        ☆ BytedAd Splash Screen example Powered By HaXiBiao ☆
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
          const splashAd = ad.loadSplashAd('5016582', '816582039');

          splashAd.subscribe('onAdTimeOver', event => {
            console.log('广告时间结束监听', event);
          });

          splashAd.subscribe('onAdSkip', i => {
            console.log('用户点击跳过监听', i);
          });

          splashAd.subscribe('onError', e => {
            console.log('开屏加载失败监听', e);
          });

          splashAd.subscribe('onAdClicked', e => {
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
