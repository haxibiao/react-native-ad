import React from 'react';
import {StyleSheet, Text, View, TouchableOpacity} from 'react-native';
import ad, {TTAd} from 'react-native-ad';

export default function TTSplashDemo() {
  return (
    <View style={styles.container}>
      <Text style={styles.welcome}>☆BytedAd example☆</Text>
      <TouchableOpacity
        style={{
          marginVertical: 20,
          paddingHorizontal: 30,
          paddingVertical: 15,
          backgroundColor: '#F96',
          borderRadius: 50,
        }}
        onPress={() => {
          const splashAd = TTAd.loadSplashAd('5016582', '816582039');

          splashAd.subscrib('onAdTimeOver', event => {
            console.log('广告时间结束监听', event);
          });

          splashAd.subscrib('onAdSkip', i => {
            console.log('用户点击跳过监听', i);
          });

          splashAd.subscrib('onError', e => {
            console.log('开屏加载失败监听', e);
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
