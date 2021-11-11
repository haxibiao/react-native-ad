import React, { useState } from 'react';
import { requireNativeComponent, StyleSheet } from 'react-native';

const StreamComponent = requireNativeComponent('StreamAd');

export interface StreamProps {
    codeid: string;
    visible?: boolean;
    onAdError?: Function;
    onAdShow?: Function;
    onAdClick?: Function;
}

export const Stream = (props: StreamProps) => {
    const { codeid, onAdError, onAdShow, onAdClick, visible = true } = props;
    if (!visible) return null;
    return (
        <StreamComponent
            codeid={codeid}
            style={{ ...styles.container }}
            onAdError={(e: any) => {
                console.log('onAdError DrawFeed', e.nativeEvent);
                onAdError && onAdError(e.nativeEvent);
            }}
            onAdClick={(e: any) => {
                console.log('onAdClick DrawFeed', e.nativeEvent);
                onAdClick && onAdClick(e.nativeEvent);
            }}
            onAdShow={(e: any) => {
                console.log('onAdShow DrawFeed', e.nativeEvent);
                onAdShow && onAdShow(e.nativeEvent);
            }}
        />
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        width: '100%',
    },
});

export default Stream;
