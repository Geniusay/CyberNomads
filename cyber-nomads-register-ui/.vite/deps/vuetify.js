import {
  DateAdapterSymbol,
  createDate
} from "./chunk-DBTYHSAV.js";
import {
  DisplaySymbol,
  IconSymbol,
  LocaleSymbol,
  ThemeSymbol,
  createDisplay,
  createIcons,
  createLocale,
  createTheme,
  useDisplay,
  useLayout,
  useLocale,
  useRtl,
  useTheme
} from "./chunk-EFJNGLWU.js";
import {
  DefaultsSymbol,
  IN_BROWSER,
  createDefaults,
  defineComponent,
  getUid,
  mergeDeep,
  useDefaults
} from "./chunk-DVMD2NVO.js";
import {
  nextTick,
  reactive
} from "./chunk-SJGJBLN5.js";
import "./chunk-5WRI5ZAA.js";

// node_modules/vuetify/lib/framework.mjs
function createVuetify() {
  let vuetify = arguments.length > 0 && arguments[0] !== void 0 ? arguments[0] : {};
  const {
    blueprint,
    ...rest
  } = vuetify;
  const options = mergeDeep(blueprint, rest);
  const {
    aliases = {},
    components = {},
    directives = {}
  } = options;
  const defaults = createDefaults(options.defaults);
  const display = createDisplay(options.display, options.ssr);
  const theme = createTheme(options.theme);
  const icons = createIcons(options.icons);
  const locale = createLocale(options.locale);
  const date = createDate(options.date);
  const install = (app) => {
    for (const key in directives) {
      app.directive(key, directives[key]);
    }
    for (const key in components) {
      app.component(key, components[key]);
    }
    for (const key in aliases) {
      app.component(key, defineComponent({
        ...aliases[key],
        name: key,
        aliasName: aliases[key].name
      }));
    }
    theme.install(app);
    app.provide(DefaultsSymbol, defaults);
    app.provide(DisplaySymbol, display);
    app.provide(ThemeSymbol, theme);
    app.provide(IconSymbol, icons);
    app.provide(LocaleSymbol, locale);
    app.provide(DateAdapterSymbol, date);
    if (IN_BROWSER && options.ssr) {
      if (app.$nuxt) {
        app.$nuxt.hook("app:suspense:resolve", () => {
          display.update();
        });
      } else {
        const {
          mount
        } = app;
        app.mount = function() {
          const vm = mount(...arguments);
          nextTick(() => display.update());
          app.mount = mount;
          return vm;
        };
      }
    }
    getUid.reset();
    if (typeof __VUE_OPTIONS_API__ !== "boolean" || __VUE_OPTIONS_API__) {
      app.mixin({
        computed: {
          $vuetify() {
            return reactive({
              defaults: inject.call(this, DefaultsSymbol),
              display: inject.call(this, DisplaySymbol),
              theme: inject.call(this, ThemeSymbol),
              icons: inject.call(this, IconSymbol),
              locale: inject.call(this, LocaleSymbol),
              date: inject.call(this, DateAdapterSymbol)
            });
          }
        }
      });
    }
  };
  return {
    install,
    defaults,
    display,
    theme,
    icons,
    locale,
    date
  };
}
var version = "3.3.12";
createVuetify.version = version;
function inject(key) {
  var _a, _b;
  const vm = this.$;
  const provides = ((_a = vm.parent) == null ? void 0 : _a.provides) ?? ((_b = vm.vnode.appContext) == null ? void 0 : _b.provides);
  if (provides && key in provides) {
    return provides[key];
  }
}
export {
  createVuetify,
  useDefaults,
  useDisplay,
  useLayout,
  useLocale,
  useRtl,
  useTheme,
  version
};
//# sourceMappingURL=vuetify.js.map
