import menuApps from "./menus/apps.menu";
import menuPages from "./menus/pages.menu";
import menuData from "./menus/data.menu";

export default {
  menu: [
    {
      text: "",
      key: "",
      items: [
        {
          key: "menu.dashboard",
          text: "Dashboard",
          link: "/workplace/dashboard",
          icon: "mdi-view-dashboard-outline",
        },
      ],
    },
    // {
    //   text: "chatgpt",
    //   items: menuAi,
    // },
    {
      text: "Apps",
      items: menuData,
    },
    {
      text: "Pages",
      key: "menu.pages",
      items: menuPages,
    }
  ],
};
