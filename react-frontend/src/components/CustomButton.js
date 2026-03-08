import React from 'react';
import styles from "./custom-styles.module.css"
import CustomText from "./CustomText"
import { COLORS } from '../constants/COLORS';

function CustomButton({label, onClick, style={}, color=COLORS.buttonPrimary, size="md", icon = null, disabled=false}){
    const disabledOpacity = disabled ? 0.5 : 1;
    return (
        <button
        disabled={disabled}
        className={`${styles.custom_button} ${styles[size]}`}
        style={{ ...style, backgroundColor: color, opacity: disabledOpacity}}
        onClick={onClick}
        >
        {/* <CustomText type="button">{label}</CustomText> */}
        {label}
        {icon && <span>{icon}</span>}
        </button>
    )
}

export default CustomButton;