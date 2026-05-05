import {notifications} from "@mantine/notifications";

export const notify = (title: string, text: string) => {
    notifications.show({
        autoClose: 2000,
        title: title,
        message: text,
        color: "navy"
    })
};