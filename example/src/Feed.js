import React, {useEffect} from 'react';
import {StyleSheet, Text, View} from 'react-native';
import {ad} from 'react-native-ad';

export default function Feed() {
  useEffect(() => {
    ad.init({
      appid: '5016582',
    });

    //提前加载feed ad 结果测试
    ad.loadFeedAd({
      appid: '5016582',
      codeid: '916582486',
    }).then(
      (result) => {
        console.log('load feed ad result ', result);
      },
      (reason) => {
        console.log('加载 feed ad 失败理由 ', reason);
      },
    );

    return () => {};
  }, []);
  return (
    <View style={styles.container}>
      <Text>测试FeedAd</Text>
      <ad.Feed
        codeid="916582486" //必传 广告位 codeid 注意区分 Android 和 IOS
        adWidth={400}
        onAdLayout={(data) => {
          console.log('Feed 广告加载成功！', data);
        }}
        onAdClose={(data) => {
          console.log('Feed 广告关闭！', data);
        }}
        onAdError={(err) => {
          console.log('Feed 广告加载失败！', err);
        }}
        onAdClick={(val) => {
          console.log('Feed 广告被用户点击！', val);
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
    backgroundColor: '#cccccc',
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
