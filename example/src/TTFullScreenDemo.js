import React from 'react';
import {StyleSheet, Text, View, TouchableOpacity} from 'react-native';
import ad, {TTAd} from 'react-native-ad';

export default function TTFullScreenDemo() {
  return (
    <View style={styles.container}>
      <Text style={styles.welcome}>
        ☆ BytedAd Full Screen Video example, Powered By HaXiBiao ☆
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
          TTAd.loadFullVideoAd('5016582', '916582815').then(val => {
            console.log('FullVideoAd', val);
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
