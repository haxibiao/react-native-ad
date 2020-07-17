import React from "react";
import { requireNativeComponent, StyleSheet } from "react-native";

const NativeFeedAd = requireNativeComponent("FeedAd");

interface Props {
  codeId: string;
  // useCache: boolean;
  adWidth: number;
  visible: boolean;
  visibleHandler: Function;
  onError?: Function;
  onLoad?: Function;
  onClick?: Function;
}

const FeedAd = (props: Props) => {
  const {
    codeId: codeid_feed,
    // useCache = true,
    adWidth = 150,
    onError,
    onLoad,
    onClick,
  } = props;
  // let [visible, setVisible] = useState(true);
  const { visible, visibleHandler } = props; // 状态交友父组件来控制，使得广告显示状态在父组件中可以实时监听
  const [height, setHeight] = React.useState(0); // 默认高度
  if (!visible) return null;
  return (
    <NativeFeedAd
      // provider={feed_provider}
      codeid={codeid_feed}
      // useCache={useCache}
      adWidth={adWidth}
      style={{ width: adWidth, height }}
      onError={(e: any) => {
        // console.log("onError feed", e.nativeEvent);
        visibleHandler(false);
        onError && onError(e.nativeEvent);
      }}
      onAdClick={(e: any) => {
        // console.log("onClick FeedAd ");
        onClick && onClick(e.nativeEvent);
      }}
      onAdClosed={(e: any) => {
        // console.log("onAdClosed", e.nativeEvent);
        visibleHandler(false);
      }}
      onLayoutChanged={(e: any) => {
        // console.log("onLayoutChanged feed", e.nativeEvent);
        if (e.nativeEvent.height) {
          setHeight(e.nativeEvent.height);
          onLoad && onLoad(e.nativeEvent);
        }
      }}
    />
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    width: "100%",
  },
});

export default FeedAd;
