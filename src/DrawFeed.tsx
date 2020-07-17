import React from "react";
import { requireNativeComponent, StyleSheet } from "react-native";

const NativeDrawFeedAd = requireNativeComponent("DrawFeed");

// 默认很多是用旧的 native 方式申请的 drawfeed 代码位...
// isExpress 用来区分是否用原生方式渲染

interface Props {
  codeId: string;
  isExpress?: boolean;
  onError?: Function;
  onLoad?: Function;
  onClick?: Function;
}

export const DrawFeedAd = (props: Props) => {
  const { codeId, isExpress, onError, onLoad, onClick } = props;
  const [visible, setVisible] = React.useState(true);

  const draw_video_provider = "";
  let codeid_draw_video = codeId;

  return (
    visible && (
      <NativeDrawFeedAd
        provider={draw_video_provider}
        is_express={isExpress ? "true" : "false"}
        codeid={codeid_draw_video}
        style={{ ...styles.container }}
        onAdError={(e: any) => {
          console.log("onError feed", e.nativeEvent);
          setVisible(false);
          onError && onError(e.nativeEvent);
        }}
        onAdClick={onClick}
        onAdShow={(e: any) => {
          console.log("onAdShow", e.nativeEvent);
        }}
      />
    )
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    width: "100%",
  },
});

export default DrawFeedAd;
