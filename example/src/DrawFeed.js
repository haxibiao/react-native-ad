import React, {useEffect} from 'react';
import {StyleSheet, Text, View, TouchableOpacity} from 'react-native';
import {ad} from 'react-native-ad';

export default function DrawFeed() {
  useEffect(() => {
    return () => {
      ad.init({
        appid: '5016582',
      });
    };
  }, []);
  return (
    <View style={styles.container}>
      <ad.DrawFeed
        codeid="945339778" // 广告位 codeid （必传），注意区分 Android 和 IOS
        onLoad={msg => {
          // 广告加载成功回调
          console.log('Draw Feed 广告加载成功！', msg);
        }}
        onError={err => {
          // 广告加载失败回调
          console.log('Draw Feed 广告加载失败！', err);
        }}
        onClick={val => {
          // 广告点击回调
          console.log('Draw Feed 广告被用户点击！', val);
        }}
      />
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
