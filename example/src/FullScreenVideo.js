import React from 'react';
import {StyleSheet, Text, View, TouchableOpacity} from 'react-native';
import {ad} from 'react-native-ad';

export default function FullVideo() {
  return (
    <View style={styles.container}>
      <Text style={styles.welcome}>
        ☆ Full Screen Video example, Powered By HaXiBiao ☆
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
          let fullVideo = ad.startFullVideo('5016582', '945294087');
          console.log('FullVideoAd rs:', fullVideo);
          fullVideo.result?.then(val => {
            console.log('FullVideoAd', val);
          });

          fullVideo.subscribe('onAdLoaded', event => {
            console.log('广告加载成功监听111111', event);
          });

          fullVideo.subscribe('onAdError', event => {
            console.log('广告加载失败监听', event);
          });

          fullVideo.subscribe('onAdClose', event => {
            console.log('广告被关闭监听', event);
          });

          fullVideo.subscribe('onAdClicked', event => {
            console.log('广告点击查看详情监听', event);
          });
        }}>
        <Text style={{textAlign: 'center'}}> Start FullVideoAd</Text>
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
