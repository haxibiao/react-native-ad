import React from 'react';
import {StyleSheet, Text, View, TouchableOpacity} from 'react-native';
import ad, {TTAd} from 'react-native-ad';

export default function TTRewardVideoDemo() {
  return (
    <View style={styles.container}>
      <Text style={styles.welcome}>
        ☆ BytedAd Reward Video example, Powered By HaXiBiao ☆
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
          const rewardVideo = TTAd.loadRewardVideoAd('5016582', '916582412');

          rewardVideo.then(val => {
            console.log('FullVideoAd', val);
          });

          rewardVideo.subscrib('onAdLoaded', event => {
            console.log('广告加载成功监听111111', event);
          });

          rewardVideo.subscrib('onAdError', event => {
            console.log('广告加载失败监听', event);
          });

          rewardVideo.subscrib('onAdClose', event => {
            console.log('广告被关闭监听', event);
          });

          rewardVideo.subscrib('onAdClicked', event => {
            console.log('广告点击查看详情监听', event);
          });


        }}>
        <Text style={{textAlign: 'center'}}> Start RewardVideoAd</Text>
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
