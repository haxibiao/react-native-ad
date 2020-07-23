import React from 'react';
import {StyleSheet, Text, View, TouchableOpacity} from 'react-native';
import {ad} from 'react-native-ad';

export default function Feed() {
  React.useEffect(() => {
    // 初始化 SDK 传入 appid ，已经初始化过的可以忽略，注意区分 Android 和 IOS
    ad.init('5016582');
  }, []);

  return (
    <View style={styles.container}>
      <ad.Feed
        codeId="916582486" // 广告位 codeid （必传），注意区分 Android 和 IOS
        visible={true}
        adWidth={300}
        onLoad={smg => {
          // 广告加载成功回调
          console.log('头条 Feed 广告加载成功！', smg);
        }}
        onError={err => {
          // 广告加载失败回调
          console.log('头条 Feed 广告加载失败！', err);
        }}
        onClick={val => {
          // 广告点击回调
          console.log('头条 Feed 广告被用户点击！', val);
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
