import React, {useEffect} from 'react';
import {StyleSheet, Text, View, TouchableOpacity} from 'react-native';
import {ad} from 'react-native-ad';

export default function RewardVideo() {
  useEffect(() => {
    ad.init({
      appid: '5016582',
      codeid_reward_video: '945294086',
    });
    return () => {};
  }, []);
  return (
    <View style={styles.container}>
      <Text style={styles.welcome}>
        ☆ Reward Video example, Powered By HaXiBiao ☆
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
          const rewardVideo = ad.startRewardVideo({
            appid: '5016582',
            codeid: '945294086',
          });

          rewardVideo.result.then((val) => {
            console.log('RewardVideo 回调结果', val);
          });

          rewardVideo.subscribe('onAdLoaded', (event) => {
            console.log('广告加载成功监听', event);
          });

          rewardVideo.subscribe('onAdError', (event) => {
            console.log('广告加载失败监听', event);
          });

          rewardVideo.subscribe('onAdClose', (event) => {
            console.log('广告被关闭监听', event);
          });

          rewardVideo.subscribe('onAdClick', (event) => {
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
