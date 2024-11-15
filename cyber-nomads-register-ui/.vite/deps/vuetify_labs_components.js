import {
  getWeek,
  toIso,
  useDate
} from "./chunk-DBTYHSAV.js";
import {
  LoaderSlot,
  MaybeTransition,
  VAvatar,
  VBtn,
  VCard,
  VCheckboxBtn,
  VDefaultsProvider,
  VDialog,
  VDivider,
  VFadeTransition,
  VField,
  VIcon,
  VOverlay,
  VProgressCircular,
  VSelect,
  VSheet,
  VSpacer,
  VTable,
  VTextField,
  VVirtualScrollItem,
  VWindow,
  VWindowItem,
  makeDimensionProps,
  makeElevationProps,
  makeFilterProps,
  makeFocusProps,
  makeGroupItemProps,
  makeGroupProps,
  makeLoaderProps,
  makeTagProps,
  makeTransitionProps,
  makeVDialogProps,
  makeVFieldProps,
  makeVSheetProps,
  makeVTableProps,
  makeVWindowItemProps,
  makeVWindowProps,
  makeVirtualProps,
  useBackgroundColor,
  useDimension,
  useElevation,
  useFilter,
  useFocus,
  useGroup,
  useGroupItem,
  useIntersectionObserver,
  useLoader,
  useVirtual
} from "./chunk-TWNB6K2Y.js";
import {
  IconValue,
  makeThemeProps,
  provideTheme,
  useLocale,
  useProxiedModel
} from "./chunk-EFJNGLWU.js";
import {
  Ripple
} from "./chunk-YXSZ2AEU.js";
import {
  IN_BROWSER,
  clamp,
  consoleWarn,
  convertToUnit,
  createRange,
  createSimpleFunctional,
  deepEqual,
  defineComponent,
  defineFunctionalComponent,
  filterInputAttrs,
  focusChild,
  genericComponent,
  getCurrentInstance,
  getObjectValueByPath,
  getPropertyFromItem,
  isEmpty,
  makeComponentProps,
  omit,
  only,
  propsFactory,
  provideDefaults,
  useRender,
  wrapInArray
} from "./chunk-DVMD2NVO.js";
import {
  Fragment,
  computed,
  createTextVNode,
  createVNode,
  inject,
  mergeProps,
  nextTick,
  onMounted,
  provide,
  ref,
  resolveDirective,
  shallowRef,
  toRef,
  toRefs,
  watch,
  watchEffect,
  withDirectives,
  withModifiers
} from "./chunk-SJGJBLN5.js";
import "./chunk-5WRI5ZAA.js";

// node_modules/vuetify/lib/labs/VBottomSheet/VBottomSheet.mjs
import "E:/Project/CyberNomads/cyber-nomads-register-ui/node_modules/vuetify/lib/labs/VBottomSheet/VBottomSheet.css";
var makeVBottomSheetProps = propsFactory({
  inset: Boolean,
  ...makeVDialogProps({
    contentClass: "v-bottom-sheet__content",
    transition: "bottom-sheet-transition"
  })
}, "VBottomSheet");
var VBottomSheet = genericComponent()({
  name: "VBottomSheet",
  props: makeVBottomSheetProps(),
  emits: {
    "update:modelValue": (value) => true
  },
  setup(props, _ref) {
    let {
      slots
    } = _ref;
    const isActive = useProxiedModel(props, "modelValue");
    useRender(() => {
      const [dialogProps] = VDialog.filterProps(props);
      return createVNode(VDialog, mergeProps(dialogProps, {
        "modelValue": isActive.value,
        "onUpdate:modelValue": ($event) => isActive.value = $event,
        "class": ["v-bottom-sheet", {
          "v-bottom-sheet--inset": props.inset
        }]
      }), slots);
    });
    return {};
  }
});

// node_modules/vuetify/lib/labs/VDataIterator/composables/items.mjs
var makeDataIteratorItemsProps = propsFactory({
  items: {
    type: Array,
    default: () => []
  },
  itemValue: {
    type: [String, Array, Function],
    default: "id"
  },
  itemSelectable: {
    type: [String, Array, Function],
    default: null
  },
  returnObject: Boolean
}, "DataIterator-items");
function transformItem(props, item) {
  const value = props.returnObject ? item : getPropertyFromItem(item, props.itemValue);
  const selectable = getPropertyFromItem(item, props.itemSelectable, true);
  return {
    type: "item",
    value,
    selectable,
    raw: item
  };
}
function transformItems(props, items) {
  const array = [];
  for (const item of items) {
    array.push(transformItem(props, item));
  }
  return array;
}
function useDataIteratorItems(props) {
  const items = computed(() => transformItems(props, props.items));
  return {
    items
  };
}

// node_modules/vuetify/lib/labs/VDataTable/composables/expand.mjs
var makeDataTableExpandProps = propsFactory({
  expandOnClick: Boolean,
  showExpand: Boolean,
  expanded: {
    type: Array,
    default: () => []
  }
}, "DataTable-expand");
var VDataTableExpandedKey = Symbol.for("vuetify:datatable:expanded");
function provideExpanded(props) {
  const expandOnClick = toRef(props, "expandOnClick");
  const expanded = useProxiedModel(props, "expanded", props.expanded, (v) => {
    return new Set(v);
  }, (v) => {
    return [...v.values()];
  });
  function expand(item, value) {
    const newExpanded = new Set(expanded.value);
    if (!value) {
      newExpanded.delete(item.value);
    } else {
      newExpanded.add(item.value);
    }
    expanded.value = newExpanded;
  }
  function isExpanded(item) {
    return expanded.value.has(item.value);
  }
  function toggleExpand(item) {
    expand(item, !isExpanded(item));
  }
  const data = {
    expand,
    expanded,
    expandOnClick,
    isExpanded,
    toggleExpand
  };
  provide(VDataTableExpandedKey, data);
  return data;
}
function useExpanded() {
  const data = inject(VDataTableExpandedKey);
  if (!data) throw new Error("foo");
  return data;
}

// node_modules/vuetify/lib/labs/VDataTable/composables/group.mjs
var makeDataTableGroupProps = propsFactory({
  groupBy: {
    type: Array,
    default: () => []
  }
}, "DataTable-group");
var VDataTableGroupSymbol = Symbol.for("vuetify:data-table-group");
function createGroupBy(props) {
  const groupBy = useProxiedModel(props, "groupBy");
  return {
    groupBy
  };
}
function provideGroupBy(options) {
  const {
    groupBy,
    sortBy
  } = options;
  const opened = ref(/* @__PURE__ */ new Set());
  const sortByWithGroups = computed(() => {
    return groupBy.value.map((val) => ({
      ...val,
      order: val.order ?? false
    })).concat(sortBy.value);
  });
  function isGroupOpen(group) {
    return opened.value.has(group.id);
  }
  function toggleGroup(group) {
    const newOpened = new Set(opened.value);
    if (!isGroupOpen(group)) newOpened.add(group.id);
    else newOpened.delete(group.id);
    opened.value = newOpened;
  }
  function extractRows(items) {
    function dive(group) {
      const arr = [];
      for (const item of group.items) {
        if ("type" in item && item.type === "group") {
          arr.push(...dive(item));
        } else {
          arr.push(item);
        }
      }
      return arr;
    }
    return dive({
      type: "group",
      items,
      id: "dummy",
      key: "dummy",
      value: "dummy",
      depth: 0
    });
  }
  const data = {
    sortByWithGroups,
    toggleGroup,
    opened,
    groupBy,
    extractRows,
    isGroupOpen
  };
  provide(VDataTableGroupSymbol, data);
  return data;
}
function useGroupBy() {
  const data = inject(VDataTableGroupSymbol);
  if (!data) throw new Error("Missing group!");
  return data;
}
function groupItemsByProperty(items, groupBy) {
  if (!items.length) return [];
  const groups = /* @__PURE__ */ new Map();
  for (const item of items) {
    const value = getObjectValueByPath(item.raw, groupBy);
    if (!groups.has(value)) {
      groups.set(value, []);
    }
    groups.get(value).push(item);
  }
  return groups;
}
function groupItems(items, groupBy) {
  let depth = arguments.length > 2 && arguments[2] !== void 0 ? arguments[2] : 0;
  let prefix = arguments.length > 3 && arguments[3] !== void 0 ? arguments[3] : "root";
  if (!groupBy.length) return [];
  const groupedItems = groupItemsByProperty(items, groupBy[0]);
  const groups = [];
  const rest = groupBy.slice(1);
  groupedItems.forEach((items2, value) => {
    const key = groupBy[0];
    const id = `${prefix}_${key}_${value}`;
    groups.push({
      depth,
      id,
      key,
      value,
      items: rest.length ? groupItems(items2, rest, depth + 1, id) : items2,
      type: "group"
    });
  });
  return groups;
}
function flattenItems(items, opened) {
  const flatItems = [];
  for (const item of items) {
    if ("type" in item && item.type === "group") {
      if (item.value != null) {
        flatItems.push(item);
      }
      if (opened.has(item.id) || item.value == null) {
        flatItems.push(...flattenItems(item.items, opened));
      }
    } else {
      flatItems.push(item);
    }
  }
  return flatItems;
}
function useGroupedItems(items, groupBy, opened) {
  const flatItems = computed(() => {
    if (!groupBy.value.length) return items.value;
    const groupedItems = groupItems(items.value, groupBy.value.map((item) => item.key));
    return flattenItems(groupedItems, opened.value);
  });
  return {
    flatItems
  };
}

// node_modules/vuetify/lib/labs/VDataTable/composables/options.mjs
function useOptions(_ref) {
  let {
    page,
    itemsPerPage,
    sortBy,
    groupBy,
    search
  } = _ref;
  const vm = getCurrentInstance("VDataTable");
  const options = computed(() => ({
    page: page.value,
    itemsPerPage: itemsPerPage.value,
    sortBy: sortBy.value,
    groupBy: groupBy.value,
    search: search.value
  }));
  watch(() => search == null ? void 0 : search.value, () => {
    page.value = 1;
  });
  let oldOptions = null;
  watch(options, () => {
    if (deepEqual(oldOptions, options.value)) return;
    vm.emit("update:options", options.value);
    oldOptions = options.value;
  }, {
    deep: true,
    immediate: true
  });
}

// node_modules/vuetify/lib/labs/VDataTable/composables/paginate.mjs
var makeDataTablePaginateProps = propsFactory({
  page: {
    type: [Number, String],
    default: 1
  },
  itemsPerPage: {
    type: [Number, String],
    default: 10
  }
}, "DataTable-paginate");
var VDataTablePaginationSymbol = Symbol.for("vuetify:data-table-pagination");
function createPagination(props) {
  const page = useProxiedModel(props, "page", void 0, (value) => +(value ?? 1));
  const itemsPerPage = useProxiedModel(props, "itemsPerPage", void 0, (value) => +(value ?? 10));
  return {
    page,
    itemsPerPage
  };
}
function providePagination(options) {
  const {
    page,
    itemsPerPage,
    itemsLength
  } = options;
  const startIndex = computed(() => {
    if (itemsPerPage.value === -1) return 0;
    return itemsPerPage.value * (page.value - 1);
  });
  const stopIndex = computed(() => {
    if (itemsPerPage.value === -1) return itemsLength.value;
    return Math.min(itemsLength.value, startIndex.value + itemsPerPage.value);
  });
  const pageCount = computed(() => {
    if (itemsPerPage.value === -1 || itemsLength.value === 0) return 1;
    return Math.ceil(itemsLength.value / itemsPerPage.value);
  });
  watchEffect(() => {
    if (page.value > pageCount.value) {
      page.value = pageCount.value;
    }
  });
  function setItemsPerPage(value) {
    itemsPerPage.value = value;
    page.value = 1;
  }
  function nextPage() {
    page.value = clamp(page.value + 1, 1, pageCount.value);
  }
  function prevPage() {
    page.value = clamp(page.value - 1, 1, pageCount.value);
  }
  function setPage(value) {
    page.value = clamp(value, 1, pageCount.value);
  }
  const data = {
    page,
    itemsPerPage,
    startIndex,
    stopIndex,
    pageCount,
    itemsLength,
    nextPage,
    prevPage,
    setPage,
    setItemsPerPage
  };
  provide(VDataTablePaginationSymbol, data);
  return data;
}
function usePagination() {
  const data = inject(VDataTablePaginationSymbol);
  if (!data) throw new Error("Missing pagination!");
  return data;
}
function usePaginatedItems(options) {
  const {
    items,
    startIndex,
    stopIndex,
    itemsPerPage
  } = options;
  const paginatedItems = computed(() => {
    if (itemsPerPage.value <= 0) return items.value;
    return items.value.slice(startIndex.value, stopIndex.value);
  });
  return {
    paginatedItems
  };
}

// node_modules/vuetify/lib/labs/VDataTable/composables/select.mjs
var singleSelectStrategy = {
  showSelectAll: false,
  allSelected: () => [],
  select: (_ref) => {
    var _a;
    let {
      items,
      value
    } = _ref;
    return new Set(value ? [(_a = items[0]) == null ? void 0 : _a.value] : []);
  },
  selectAll: (_ref2) => {
    let {
      selected
    } = _ref2;
    return selected;
  }
};
var pageSelectStrategy = {
  showSelectAll: true,
  allSelected: (_ref3) => {
    let {
      currentPage
    } = _ref3;
    return currentPage;
  },
  select: (_ref4) => {
    let {
      items,
      value,
      selected
    } = _ref4;
    for (const item of items) {
      if (value) selected.add(item.value);
      else selected.delete(item.value);
    }
    return selected;
  },
  selectAll: (_ref5) => {
    let {
      value,
      currentPage,
      selected
    } = _ref5;
    return pageSelectStrategy.select({
      items: currentPage,
      value,
      selected
    });
  }
};
var allSelectStrategy = {
  showSelectAll: true,
  allSelected: (_ref6) => {
    let {
      allItems
    } = _ref6;
    return allItems;
  },
  select: (_ref7) => {
    let {
      items,
      value,
      selected
    } = _ref7;
    for (const item of items) {
      if (value) selected.add(item.value);
      else selected.delete(item.value);
    }
    return selected;
  },
  selectAll: (_ref8) => {
    let {
      value,
      allItems,
      selected
    } = _ref8;
    return allSelectStrategy.select({
      items: allItems,
      value,
      selected
    });
  }
};
var makeDataTableSelectProps = propsFactory({
  showSelect: Boolean,
  selectStrategy: {
    type: [String, Object],
    default: "page"
  },
  modelValue: {
    type: Array,
    default: () => []
  }
}, "DataTable-select");
var VDataTableSelectionSymbol = Symbol.for("vuetify:data-table-selection");
function provideSelection(props, _ref9) {
  let {
    allItems,
    currentPage
  } = _ref9;
  const selected = useProxiedModel(props, "modelValue", props.modelValue, (v) => {
    return new Set(v);
  }, (v) => {
    return [...v.values()];
  });
  const allSelectable = computed(() => allItems.value.filter((item) => item.selectable));
  const currentPageSelectable = computed(() => currentPage.value.filter((item) => item.selectable));
  const selectStrategy = computed(() => {
    if (typeof props.selectStrategy === "object") return props.selectStrategy;
    switch (props.selectStrategy) {
      case "single":
        return singleSelectStrategy;
      case "all":
        return allSelectStrategy;
      case "page":
      default:
        return pageSelectStrategy;
    }
  });
  function isSelected(items) {
    return wrapInArray(items).every((item) => selected.value.has(item.value));
  }
  function isSomeSelected(items) {
    return wrapInArray(items).some((item) => selected.value.has(item.value));
  }
  function select(items, value) {
    const newSelected = selectStrategy.value.select({
      items,
      value,
      selected: new Set(selected.value)
    });
    selected.value = newSelected;
  }
  function toggleSelect(item) {
    select([item], !isSelected([item]));
  }
  function selectAll(value) {
    const newSelected = selectStrategy.value.selectAll({
      value,
      allItems: allSelectable.value,
      currentPage: currentPageSelectable.value,
      selected: new Set(selected.value)
    });
    selected.value = newSelected;
  }
  const someSelected = computed(() => selected.value.size > 0);
  const allSelected = computed(() => {
    const items = selectStrategy.value.allSelected({
      allItems: allSelectable.value,
      currentPage: currentPageSelectable.value
    });
    return isSelected(items);
  });
  const data = {
    toggleSelect,
    select,
    selectAll,
    isSelected,
    isSomeSelected,
    someSelected,
    allSelected,
    showSelectAll: selectStrategy.value.showSelectAll
  };
  provide(VDataTableSelectionSymbol, data);
  return data;
}
function useSelection() {
  const data = inject(VDataTableSelectionSymbol);
  if (!data) throw new Error("Missing selection!");
  return data;
}

// node_modules/vuetify/lib/labs/VDataTable/composables/sort.mjs
var makeDataTableSortProps = propsFactory({
  sortBy: {
    type: Array,
    default: () => []
  },
  customKeySort: Object,
  multiSort: Boolean,
  mustSort: Boolean
}, "DataTable-sort");
var VDataTableSortSymbol = Symbol.for("vuetify:data-table-sort");
function createSort(props) {
  const sortBy = useProxiedModel(props, "sortBy");
  const mustSort = toRef(props, "mustSort");
  const multiSort = toRef(props, "multiSort");
  return {
    sortBy,
    mustSort,
    multiSort
  };
}
function provideSort(options) {
  const {
    sortBy,
    mustSort,
    multiSort,
    page
  } = options;
  const toggleSort = (column) => {
    let newSortBy = sortBy.value.map((x) => ({
      ...x
    })) ?? [];
    const item = newSortBy.find((x) => x.key === column.key);
    if (!item) {
      if (multiSort.value) newSortBy = [...newSortBy, {
        key: column.key,
        order: "asc"
      }];
      else newSortBy = [{
        key: column.key,
        order: "asc"
      }];
    } else if (item.order === "desc") {
      if (mustSort.value) {
        item.order = "asc";
      } else {
        newSortBy = newSortBy.filter((x) => x.key !== column.key);
      }
    } else {
      item.order = "desc";
    }
    sortBy.value = newSortBy;
    if (page) page.value = 1;
  };
  function isSorted(column) {
    return !!sortBy.value.find((item) => item.key === column.key);
  }
  const data = {
    sortBy,
    toggleSort,
    isSorted
  };
  provide(VDataTableSortSymbol, data);
  return data;
}
function useSort() {
  const data = inject(VDataTableSortSymbol);
  if (!data) throw new Error("Missing sort!");
  return data;
}
function useSortedItems(props, items, sortBy) {
  const locale = useLocale();
  const sortedItems = computed(() => {
    if (!sortBy.value.length) return items.value;
    return sortItems(items.value, sortBy.value, locale.current.value, props.customKeySort);
  });
  return {
    sortedItems
  };
}
function sortItems(items, sortByItems, locale, customSorters) {
  const stringCollator = new Intl.Collator(locale, {
    sensitivity: "accent",
    usage: "sort"
  });
  return [...items].sort((a, b) => {
    for (let i = 0; i < sortByItems.length; i++) {
      const sortKey = sortByItems[i].key;
      const sortOrder = sortByItems[i].order ?? "asc";
      if (sortOrder === false) continue;
      let sortA = getObjectValueByPath(a.raw, sortKey);
      let sortB = getObjectValueByPath(b.raw, sortKey);
      if (sortOrder === "desc") {
        [sortA, sortB] = [sortB, sortA];
      }
      if (customSorters == null ? void 0 : customSorters[sortKey]) {
        const customResult = customSorters[sortKey](sortA, sortB);
        if (!customResult) continue;
        return customResult;
      }
      if (sortA instanceof Date && sortB instanceof Date) {
        return sortA.getTime() - sortB.getTime();
      }
      [sortA, sortB] = [sortA, sortB].map((s) => s != null ? s.toString().toLocaleLowerCase() : s);
      if (sortA !== sortB) {
        if (isEmpty(sortA) && isEmpty(sortB)) return 0;
        if (isEmpty(sortA)) return -1;
        if (isEmpty(sortB)) return 1;
        if (!isNaN(sortA) && !isNaN(sortB)) return Number(sortA) - Number(sortB);
        return stringCollator.compare(sortA, sortB);
      }
    }
    return 0;
  });
}

// node_modules/vuetify/lib/labs/VDataIterator/VDataIterator.mjs
var makeVDataIteratorProps = propsFactory({
  search: String,
  loading: Boolean,
  ...makeComponentProps(),
  ...makeDataIteratorItemsProps(),
  ...makeDataTableSelectProps(),
  ...makeDataTableSortProps(),
  ...makeDataTablePaginateProps({
    itemsPerPage: 5
  }),
  ...makeDataTableExpandProps(),
  ...makeDataTableGroupProps(),
  ...makeFilterProps(),
  ...makeTagProps()
}, "VDataIterator");
var VDataIterator = genericComponent()({
  name: "VDataIterator",
  props: makeVDataIteratorProps(),
  emits: {
    "update:modelValue": (value) => true,
    "update:groupBy": (value) => true,
    "update:page": (value) => true,
    "update:itemsPerPage": (value) => true,
    "update:sortBy": (value) => true,
    "update:options": (value) => true,
    "update:expanded": (value) => true
  },
  setup(props, _ref) {
    let {
      slots
    } = _ref;
    const groupBy = useProxiedModel(props, "groupBy");
    const search = toRef(props, "search");
    const {
      items
    } = useDataIteratorItems(props);
    const {
      filteredItems
    } = useFilter(props, items, search, {
      transform: (item) => item.raw
    });
    const {
      sortBy,
      multiSort,
      mustSort
    } = createSort(props);
    const {
      page,
      itemsPerPage
    } = createPagination(props);
    const {
      toggleSort
    } = provideSort({
      sortBy,
      multiSort,
      mustSort,
      page
    });
    const {
      sortByWithGroups,
      opened,
      extractRows,
      isGroupOpen,
      toggleGroup
    } = provideGroupBy({
      groupBy,
      sortBy
    });
    const {
      sortedItems
    } = useSortedItems(props, filteredItems, sortByWithGroups);
    const {
      flatItems
    } = useGroupedItems(sortedItems, groupBy, opened);
    const itemsLength = computed(() => flatItems.value.length);
    const {
      startIndex,
      stopIndex,
      pageCount,
      prevPage,
      nextPage,
      setItemsPerPage,
      setPage
    } = providePagination({
      page,
      itemsPerPage,
      itemsLength
    });
    const {
      paginatedItems
    } = usePaginatedItems({
      items: flatItems,
      startIndex,
      stopIndex,
      itemsPerPage
    });
    const paginatedItemsWithoutGroups = computed(() => extractRows(paginatedItems.value));
    const {
      isSelected,
      select,
      selectAll,
      toggleSelect
    } = provideSelection(props, {
      allItems: items,
      currentPage: paginatedItemsWithoutGroups
    });
    const {
      isExpanded,
      toggleExpand
    } = provideExpanded(props);
    useOptions({
      page,
      itemsPerPage,
      sortBy,
      groupBy,
      search
    });
    const slotProps = computed(() => ({
      page: page.value,
      itemsPerPage: itemsPerPage.value,
      sortBy: sortBy.value,
      pageCount: pageCount.value,
      toggleSort,
      prevPage,
      nextPage,
      setPage,
      setItemsPerPage,
      isSelected,
      select,
      selectAll,
      toggleSelect,
      isExpanded,
      toggleExpand,
      isGroupOpen,
      toggleGroup,
      items: paginatedItemsWithoutGroups.value,
      groupedItems: paginatedItems.value
    }));
    useRender(() => createVNode(props.tag, {
      "class": ["v-data-iterator", props.class],
      "style": props.style
    }, {
      default: () => {
        var _a, _b, _c, _d;
        return [(_a = slots.header) == null ? void 0 : _a.call(slots, slotProps.value), !paginatedItems.value.length ? (_b = slots["no-data"]) == null ? void 0 : _b.call(slots) : (_c = slots.default) == null ? void 0 : _c.call(slots, slotProps.value), (_d = slots.footer) == null ? void 0 : _d.call(slots, slotProps.value)];
      }
    }));
    return {};
  }
});

// node_modules/vuetify/lib/labs/VDataTable/VDataTable.mjs
import "E:/Project/CyberNomads/cyber-nomads-register-ui/node_modules/vuetify/lib/labs/VDataTable/VDataTable.css";

// node_modules/vuetify/lib/labs/VDataTable/VDataTableFooter.mjs
import "E:/Project/CyberNomads/cyber-nomads-register-ui/node_modules/vuetify/lib/labs/VDataTable/VDataTableFooter.css";
var makeVDataTableFooterProps = propsFactory({
  prevIcon: {
    type: String,
    default: "$prev"
  },
  nextIcon: {
    type: String,
    default: "$next"
  },
  firstIcon: {
    type: String,
    default: "$first"
  },
  lastIcon: {
    type: String,
    default: "$last"
  },
  itemsPerPageText: {
    type: String,
    default: "$vuetify.dataFooter.itemsPerPageText"
  },
  pageText: {
    type: String,
    default: "$vuetify.dataFooter.pageText"
  },
  firstPageLabel: {
    type: String,
    default: "$vuetify.dataFooter.firstPage"
  },
  prevPageLabel: {
    type: String,
    default: "$vuetify.dataFooter.prevPage"
  },
  nextPageLabel: {
    type: String,
    default: "$vuetify.dataFooter.nextPage"
  },
  lastPageLabel: {
    type: String,
    default: "$vuetify.dataFooter.lastPage"
  },
  itemsPerPageOptions: {
    type: Array,
    default: () => [{
      value: 10,
      title: "10"
    }, {
      value: 25,
      title: "25"
    }, {
      value: 50,
      title: "50"
    }, {
      value: 100,
      title: "100"
    }, {
      value: -1,
      title: "$vuetify.dataFooter.itemsPerPageAll"
    }]
  },
  showCurrentPage: Boolean
}, "VDataTableFooter");
var VDataTableFooter = genericComponent()({
  name: "VDataTableFooter",
  props: makeVDataTableFooterProps(),
  setup(props, _ref) {
    let {
      slots
    } = _ref;
    const {
      t
    } = useLocale();
    const {
      page,
      pageCount,
      startIndex,
      stopIndex,
      itemsLength,
      itemsPerPage,
      setItemsPerPage
    } = usePagination();
    const itemsPerPageOptions = computed(() => props.itemsPerPageOptions.map((option) => ({
      ...option,
      title: t(option.title)
    })));
    return () => {
      var _a;
      return createVNode("div", {
        "class": "v-data-table-footer"
      }, [(_a = slots.prepend) == null ? void 0 : _a.call(slots), createVNode("div", {
        "class": "v-data-table-footer__items-per-page"
      }, [createVNode("span", null, [t(props.itemsPerPageText)]), createVNode(VSelect, {
        "items": itemsPerPageOptions.value,
        "modelValue": itemsPerPage.value,
        "onUpdate:modelValue": (v) => setItemsPerPage(Number(v)),
        "density": "compact",
        "variant": "outlined",
        "hide-details": true
      }, null)]), createVNode("div", {
        "class": "v-data-table-footer__info"
      }, [createVNode("div", null, [t(props.pageText, !itemsLength.value ? 0 : startIndex.value + 1, stopIndex.value, itemsLength.value)])]), createVNode("div", {
        "class": "v-data-table-footer__pagination"
      }, [createVNode(VBtn, {
        "icon": props.firstIcon,
        "variant": "plain",
        "onClick": () => page.value = 1,
        "disabled": page.value === 1,
        "aria-label": t(props.firstPageLabel)
      }, null), createVNode(VBtn, {
        "icon": props.prevIcon,
        "variant": "plain",
        "onClick": () => page.value = Math.max(1, page.value - 1),
        "disabled": page.value === 1,
        "aria-label": t(props.prevPageLabel)
      }, null), props.showCurrentPage && createVNode("span", {
        "key": "page",
        "class": "v-data-table-footer__page"
      }, [page.value]), createVNode(VBtn, {
        "icon": props.nextIcon,
        "variant": "plain",
        "onClick": () => page.value = Math.min(pageCount.value, page.value + 1),
        "disabled": page.value === pageCount.value,
        "aria-label": t(props.nextPageLabel)
      }, null), createVNode(VBtn, {
        "icon": props.lastIcon,
        "variant": "plain",
        "onClick": () => page.value = pageCount.value,
        "disabled": page.value === pageCount.value,
        "aria-label": t(props.lastPageLabel)
      }, null)])]);
    };
  }
});

// node_modules/vuetify/lib/labs/VDataTable/VDataTableColumn.mjs
var VDataTableColumn = defineFunctionalComponent({
  align: {
    type: String,
    default: "start"
  },
  fixed: Boolean,
  fixedOffset: [Number, String],
  height: [Number, String],
  lastFixed: Boolean,
  noPadding: Boolean,
  tag: String,
  width: [Number, String]
}, (props, _ref) => {
  let {
    slots,
    attrs
  } = _ref;
  const Tag = props.tag ?? "td";
  return createVNode(Tag, mergeProps({
    "class": ["v-data-table__td", {
      "v-data-table-column--fixed": props.fixed,
      "v-data-table-column--last-fixed": props.lastFixed,
      "v-data-table-column--no-padding": props.noPadding
    }, `v-data-table-column--align-${props.align}`],
    "style": {
      height: convertToUnit(props.height),
      width: convertToUnit(props.width),
      left: convertToUnit(props.fixedOffset || null)
    }
  }, attrs), {
    default: () => {
      var _a;
      return [(_a = slots.default) == null ? void 0 : _a.call(slots)];
    }
  });
});

// node_modules/vuetify/lib/labs/VDataTable/composables/headers.mjs
var makeDataTableHeaderProps = propsFactory({
  headers: {
    type: Array,
    default: () => []
  }
}, "DataTable-header");
var VDataTableHeadersSymbol = Symbol.for("vuetify:data-table-headers");
function createHeaders(props, options) {
  const headers = ref([]);
  const columns = ref([]);
  watchEffect(() => {
    var _a, _b, _c;
    const wrapped = !props.headers.length ? [] : Array.isArray(props.headers[0]) ? props.headers : [props.headers];
    const flat = wrapped.flatMap((row, index) => row.map((column) => ({
      column,
      row: index
    })));
    const rowCount = wrapped.length;
    const defaultHeader = {
      title: "",
      sortable: false
    };
    const defaultActionHeader = {
      ...defaultHeader,
      width: 48
    };
    if ((_a = options == null ? void 0 : options.groupBy) == null ? void 0 : _a.value.length) {
      const index = flat.findIndex((_ref) => {
        let {
          column
        } = _ref;
        return column.key === "data-table-group";
      });
      if (index < 0) flat.unshift({
        column: {
          ...defaultHeader,
          key: "data-table-group",
          title: "Group",
          rowspan: rowCount
        },
        row: 0
      });
      else flat.splice(index, 1, {
        column: {
          ...defaultHeader,
          ...flat[index].column
        },
        row: flat[index].row
      });
    }
    if ((_b = options == null ? void 0 : options.showSelect) == null ? void 0 : _b.value) {
      const index = flat.findIndex((_ref2) => {
        let {
          column
        } = _ref2;
        return column.key === "data-table-select";
      });
      if (index < 0) flat.unshift({
        column: {
          ...defaultActionHeader,
          key: "data-table-select",
          rowspan: rowCount
        },
        row: 0
      });
      else flat.splice(index, 1, {
        column: {
          ...defaultActionHeader,
          ...flat[index].column
        },
        row: flat[index].row
      });
    }
    if ((_c = options == null ? void 0 : options.showExpand) == null ? void 0 : _c.value) {
      const index = flat.findIndex((_ref3) => {
        let {
          column
        } = _ref3;
        return column.key === "data-table-expand";
      });
      if (index < 0) flat.push({
        column: {
          ...defaultActionHeader,
          key: "data-table-expand",
          rowspan: rowCount
        },
        row: 0
      });
      else flat.splice(index, 1, {
        column: {
          ...defaultActionHeader,
          ...flat[index].column
        },
        row: flat[index].row
      });
    }
    const fixedRows = createRange(rowCount).map(() => []);
    const fixedOffsets = createRange(rowCount).fill(0);
    flat.forEach((_ref4) => {
      let {
        column,
        row
      } = _ref4;
      let key = column.key;
      if (key == null) {
        consoleWarn("The header key value must not be null or undefined");
        key = "";
      }
      for (let i = row; i <= row + (column.rowspan ?? 1) - 1; i++) {
        fixedRows[i].push({
          ...column,
          key,
          fixedOffset: fixedOffsets[i],
          sortable: column.sortable ?? !!column.key
        });
        fixedOffsets[i] += Number(column.width ?? 0);
      }
    });
    fixedRows.forEach((row) => {
      for (let i = row.length; i--; i >= 0) {
        if (row[i].fixed) {
          row[i].lastFixed = true;
          return;
        }
      }
    });
    const seen = /* @__PURE__ */ new Set();
    headers.value = fixedRows.map((row) => {
      const filtered = [];
      for (const column of row) {
        if (!seen.has(column.key)) {
          seen.add(column.key);
          filtered.push(column);
        }
      }
      return filtered;
    });
    columns.value = fixedRows.at(-1) ?? [];
  });
  const data = {
    headers,
    columns
  };
  provide(VDataTableHeadersSymbol, data);
  return data;
}
function useHeaders() {
  const data = inject(VDataTableHeadersSymbol);
  if (!data) throw new Error("Missing headers!");
  return data;
}

// node_modules/vuetify/lib/labs/VDataTable/VDataTableHeaders.mjs
var makeVDataTableHeadersProps = propsFactory({
  color: String,
  sticky: Boolean,
  multiSort: Boolean,
  sortAscIcon: {
    type: IconValue,
    default: "$sortAsc"
  },
  sortDescIcon: {
    type: IconValue,
    default: "$sortDesc"
  },
  ...makeLoaderProps()
}, "VDataTableHeaders");
var VDataTableHeaders = genericComponent()({
  name: "VDataTableHeaders",
  props: makeVDataTableHeadersProps(),
  setup(props, _ref) {
    let {
      slots,
      emit
    } = _ref;
    const {
      toggleSort,
      sortBy,
      isSorted
    } = useSort();
    const {
      someSelected,
      allSelected,
      selectAll,
      showSelectAll
    } = useSelection();
    const {
      columns,
      headers
    } = useHeaders();
    const {
      loaderClasses
    } = useLoader(props);
    const getFixedStyles = (column, y) => {
      if (!props.sticky && !column.fixed) return void 0;
      return {
        position: "sticky",
        zIndex: column.fixed ? 4 : props.sticky ? 3 : void 0,
        // TODO: This needs to account for possible previous fixed columns.
        left: column.fixed ? convertToUnit(column.fixedOffset) : void 0,
        // TODO: This needs to account for possible row/colspan of previous columns
        top: props.sticky ? `calc(var(--v-table-header-height) * ${y})` : void 0
      };
    };
    function getSortIcon(column) {
      const item = sortBy.value.find((item2) => item2.key === column.key);
      if (!item) return props.sortAscIcon;
      return item.order === "asc" ? props.sortAscIcon : props.sortDescIcon;
    }
    const {
      backgroundColorClasses,
      backgroundColorStyles
    } = useBackgroundColor(props, "color");
    const slotProps = computed(() => ({
      headers: headers.value,
      columns: columns.value,
      toggleSort,
      isSorted,
      sortBy: sortBy.value,
      someSelected: someSelected.value,
      allSelected: allSelected.value,
      selectAll,
      getSortIcon,
      getFixedStyles
    }));
    const VDataTableHeaderCell = (_ref2) => {
      let {
        column,
        x,
        y
      } = _ref2;
      const noPadding = column.key === "data-table-select" || column.key === "data-table-expand";
      return createVNode(VDataTableColumn, {
        "tag": "th",
        "align": column.align,
        "class": ["v-data-table__th", {
          "v-data-table__th--sortable": column.sortable,
          "v-data-table__th--sorted": isSorted(column)
        }, loaderClasses.value],
        "style": {
          width: convertToUnit(column.width),
          minWidth: convertToUnit(column.width),
          ...getFixedStyles(column, y)
        },
        "colspan": column.colspan,
        "rowspan": column.rowspan,
        "onClick": column.sortable ? () => toggleSort(column) : void 0,
        "lastFixed": column.lastFixed,
        "noPadding": noPadding
      }, {
        default: () => {
          var _a;
          const columnSlotName = `column.${column.key}`;
          const columnSlotProps = {
            column,
            selectAll,
            isSorted,
            toggleSort,
            sortBy: sortBy.value,
            someSelected: someSelected.value,
            allSelected: allSelected.value,
            getSortIcon
          };
          if (slots[columnSlotName]) return slots[columnSlotName](columnSlotProps);
          if (column.key === "data-table-select") {
            return ((_a = slots["column.data-table-select"]) == null ? void 0 : _a.call(slots, columnSlotProps)) ?? (showSelectAll && createVNode(VCheckboxBtn, {
              "modelValue": allSelected.value,
              "indeterminate": someSelected.value && !allSelected.value,
              "onUpdate:modelValue": selectAll
            }, null));
          }
          return createVNode("div", {
            "class": "v-data-table-header__content"
          }, [createVNode("span", null, [column.title]), column.sortable && createVNode(VIcon, {
            "key": "icon",
            "class": "v-data-table-header__sort-icon",
            "icon": getSortIcon(column)
          }, null), props.multiSort && isSorted(column) && createVNode("div", {
            "key": "badge",
            "class": ["v-data-table-header__sort-badge", ...backgroundColorClasses.value],
            "style": backgroundColorStyles.value
          }, [sortBy.value.findIndex((x2) => x2.key === column.key) + 1])]);
        }
      });
    };
    useRender(() => {
      return createVNode(Fragment, null, [slots.headers ? slots.headers(slotProps.value) : headers.value.map((row, y) => createVNode("tr", null, [row.map((column, x) => createVNode(VDataTableHeaderCell, {
        "column": column,
        "x": x,
        "y": y
      }, null))])), props.loading && createVNode("tr", {
        "class": "v-data-table-progress"
      }, [createVNode("th", {
        "colspan": columns.value.length
      }, [createVNode(LoaderSlot, {
        "name": "v-data-table-progress",
        "active": true,
        "color": typeof props.loading === "boolean" ? void 0 : props.loading,
        "indeterminate": true
      }, {
        default: slots.loader
      })])])]);
    });
  }
});

// node_modules/vuetify/lib/labs/VDataTable/VDataTableGroupHeaderRow.mjs
var makeVDataTableGroupHeaderRowProps = propsFactory({
  item: {
    type: Object,
    required: true
  }
}, "VDataTableGroupHeaderRow");
var VDataTableGroupHeaderRow = genericComponent()({
  name: "VDataTableGroupHeaderRow",
  props: makeVDataTableGroupHeaderRowProps(),
  setup(props, _ref) {
    let {
      slots
    } = _ref;
    const {
      isGroupOpen,
      toggleGroup,
      extractRows
    } = useGroupBy();
    const {
      isSelected,
      isSomeSelected,
      select
    } = useSelection();
    const {
      columns
    } = useHeaders();
    const rows = computed(() => {
      return extractRows([props.item]);
    });
    return () => createVNode("tr", {
      "class": "v-data-table-group-header-row",
      "style": {
        "--v-data-table-group-header-row-depth": props.item.depth
      }
    }, [columns.value.map((column) => {
      var _a, _b;
      if (column.key === "data-table-group") {
        const icon = isGroupOpen(props.item) ? "$expand" : "$next";
        const onClick = () => toggleGroup(props.item);
        return ((_a = slots["data-table-group"]) == null ? void 0 : _a.call(slots, {
          item: props.item,
          count: rows.value.length,
          props: {
            icon,
            onClick
          }
        })) ?? createVNode(VDataTableColumn, {
          "class": "v-data-table-group-header-row__column"
        }, {
          default: () => [createVNode(VBtn, {
            "size": "small",
            "variant": "text",
            "icon": icon,
            "onClick": onClick
          }, null), createVNode("span", null, [props.item.value]), createVNode("span", null, [createTextVNode("("), rows.value.length, createTextVNode(")")])]
        });
      }
      if (column.key === "data-table-select") {
        const modelValue = isSelected(rows.value);
        const indeterminate = isSomeSelected(rows.value) && !modelValue;
        const selectGroup = (v) => select(rows.value, v);
        return ((_b = slots["data-table-select"]) == null ? void 0 : _b.call(slots, {
          props: {
            modelValue,
            indeterminate,
            "onUpdate:modelValue": selectGroup
          }
        })) ?? createVNode("td", null, [createVNode(VCheckboxBtn, {
          "modelValue": modelValue,
          "indeterminate": indeterminate,
          "onUpdate:modelValue": selectGroup
        }, null)]);
      }
      return createVNode("td", null, null);
    })]);
  }
});

// node_modules/vuetify/lib/labs/VDataTable/VDataTableRow.mjs
var makeVDataTableRowProps = propsFactory({
  index: Number,
  item: Object,
  onClick: Function
}, "VDataTableRow");
var VDataTableRow = defineComponent({
  name: "VDataTableRow",
  props: makeVDataTableRowProps(),
  setup(props, _ref) {
    let {
      slots
    } = _ref;
    const {
      isSelected,
      toggleSelect
    } = useSelection();
    const {
      isExpanded,
      toggleExpand
    } = useExpanded();
    const {
      columns
    } = useHeaders();
    useRender(() => createVNode("tr", {
      "class": ["v-data-table__tr", {
        "v-data-table__tr--clickable": !!props.onClick
      }],
      "onClick": props.onClick
    }, [props.item && columns.value.map((column, i) => createVNode(VDataTableColumn, {
      "align": column.align,
      "fixed": column.fixed,
      "fixedOffset": column.fixedOffset,
      "lastFixed": column.lastFixed,
      "noPadding": column.key === "data-table-select" || column.key === "data-table-expand",
      "width": column.width
    }, {
      default: () => {
        var _a, _b;
        const item = props.item;
        const slotName = `item.${column.key}`;
        const slotProps = {
          index: props.index,
          item: props.item,
          columns: columns.value,
          isSelected,
          toggleSelect,
          isExpanded,
          toggleExpand
        };
        if (slots[slotName]) return slots[slotName](slotProps);
        if (column.key === "data-table-select") {
          return ((_a = slots["item.data-table-select"]) == null ? void 0 : _a.call(slots, slotProps)) ?? createVNode(VCheckboxBtn, {
            "disabled": !item.selectable,
            "modelValue": isSelected([item]),
            "onClick": withModifiers(() => toggleSelect(item), ["stop"])
          }, null);
        }
        if (column.key === "data-table-expand") {
          return ((_b = slots["item.data-table-expand"]) == null ? void 0 : _b.call(slots, slotProps)) ?? createVNode(VBtn, {
            "icon": isExpanded(item) ? "$collapse" : "$expand",
            "size": "small",
            "variant": "text",
            "onClick": withModifiers(() => toggleExpand(item), ["stop"])
          }, null);
        }
        return getPropertyFromItem(item.columns, column.key);
      }
    }))]));
  }
});

// node_modules/vuetify/lib/labs/VDataTable/VDataTableRows.mjs
var makeVDataTableRowsProps = propsFactory({
  loading: [Boolean, String],
  loadingText: {
    type: String,
    default: "$vuetify.dataIterator.loadingText"
  },
  hideNoData: Boolean,
  items: {
    type: Array,
    default: () => []
  },
  noDataText: {
    type: String,
    default: "$vuetify.noDataText"
  },
  rowHeight: Number,
  "onClick:row": Function
}, "VDataTableRows");
var VDataTableRows = genericComponent()({
  name: "VDataTableRows",
  props: makeVDataTableRowsProps(),
  setup(props, _ref) {
    let {
      emit,
      slots
    } = _ref;
    const {
      columns
    } = useHeaders();
    const {
      expandOnClick,
      toggleExpand,
      isExpanded
    } = useExpanded();
    const {
      isSelected,
      toggleSelect
    } = useSelection();
    const {
      toggleGroup,
      isGroupOpen
    } = useGroupBy();
    const {
      t
    } = useLocale();
    useRender(() => {
      var _a;
      if (props.loading && slots.loading) {
        return createVNode("tr", {
          "class": "v-data-table-rows-loading",
          "key": "loading"
        }, [createVNode("td", {
          "colspan": columns.value.length
        }, [slots.loading()])]);
      }
      if (!props.loading && !props.items.length && !props.hideNoData) {
        return createVNode("tr", {
          "class": "v-data-table-rows-no-data",
          "key": "no-data"
        }, [createVNode("td", {
          "colspan": columns.value.length
        }, [((_a = slots["no-data"]) == null ? void 0 : _a.call(slots)) ?? t(props.noDataText)])]);
      }
      return createVNode(Fragment, null, [props.items.map((item, index) => {
        var _a2;
        if (item.type === "group") {
          return slots["group-header"] ? slots["group-header"]({
            index,
            item,
            columns: columns.value,
            isExpanded,
            toggleExpand,
            isSelected,
            toggleSelect,
            toggleGroup,
            isGroupOpen
          }) : createVNode(VDataTableGroupHeaderRow, {
            "key": `group-header_${item.id}`,
            "item": item
          }, slots);
        }
        const slotProps = {
          index,
          item,
          columns: columns.value,
          isExpanded,
          toggleExpand,
          isSelected,
          toggleSelect
        };
        const itemSlotProps = {
          ...slotProps,
          props: {
            key: `item_${item.key ?? item.index}`,
            onClick: expandOnClick.value || props["onClick:row"] ? (event) => {
              var _a3;
              if (expandOnClick.value) {
                toggleExpand(item);
              }
              (_a3 = props["onClick:row"]) == null ? void 0 : _a3.call(props, event, {
                item
              });
            } : void 0,
            index,
            item
          }
        };
        return createVNode(Fragment, null, [slots.item ? slots.item(itemSlotProps) : createVNode(VDataTableRow, itemSlotProps.props, slots), isExpanded(item) && ((_a2 = slots["expanded-row"]) == null ? void 0 : _a2.call(slots, slotProps))]);
      })]);
    });
    return {};
  }
});

// node_modules/vuetify/lib/labs/VDataTable/composables/items.mjs
var makeDataTableItemsProps = propsFactory({
  items: {
    type: Array,
    default: () => []
  },
  itemValue: {
    type: [String, Array, Function],
    default: "id"
  },
  itemSelectable: {
    type: [String, Array, Function],
    default: null
  },
  returnObject: Boolean
}, "DataTable-items");
function transformItem2(props, item, index, columns) {
  const value = props.returnObject ? item : getPropertyFromItem(item, props.itemValue);
  const selectable = getPropertyFromItem(item, props.itemSelectable, true);
  const itemColumns = columns.reduce((obj, column) => {
    obj[column.key] = getPropertyFromItem(item, column.value ?? column.key);
    return obj;
  }, {});
  return {
    type: "item",
    key: props.returnObject ? getPropertyFromItem(item, props.itemValue) : value,
    index,
    value,
    selectable,
    columns: itemColumns,
    raw: item
  };
}
function transformItems2(props, items, columns) {
  return items.map((item, index) => transformItem2(props, item, index, columns));
}
function useDataTableItems(props, columns) {
  const items = computed(() => transformItems2(props, props.items, columns.value));
  return {
    items
  };
}

// node_modules/vuetify/lib/labs/VDataTable/VDataTable.mjs
var makeDataTableProps = propsFactory({
  ...makeVDataTableRowsProps(),
  width: [String, Number],
  search: String,
  ...makeDataTableExpandProps(),
  ...makeDataTableGroupProps(),
  ...makeDataTableHeaderProps(),
  ...makeDataTableItemsProps(),
  ...makeDataTableSelectProps(),
  ...makeDataTableSortProps(),
  ...makeVDataTableHeadersProps(),
  ...makeVTableProps()
}, "DataTable");
var makeVDataTableProps = propsFactory({
  ...makeDataTablePaginateProps(),
  ...makeDataTableProps(),
  ...makeFilterProps(),
  ...makeVDataTableFooterProps()
}, "VDataTable");
var VDataTable = genericComponent()({
  name: "VDataTable",
  props: makeVDataTableProps(),
  emits: {
    "update:modelValue": (value) => true,
    "update:page": (value) => true,
    "update:itemsPerPage": (value) => true,
    "update:sortBy": (value) => true,
    "update:options": (value) => true,
    "update:groupBy": (value) => true,
    "update:expanded": (value) => true
  },
  setup(props, _ref) {
    let {
      emit,
      slots
    } = _ref;
    const {
      groupBy
    } = createGroupBy(props);
    const {
      sortBy,
      multiSort,
      mustSort
    } = createSort(props);
    const {
      page,
      itemsPerPage
    } = createPagination(props);
    const {
      columns,
      headers
    } = createHeaders(props, {
      groupBy,
      showSelect: toRef(props, "showSelect"),
      showExpand: toRef(props, "showExpand")
    });
    const {
      items
    } = useDataTableItems(props, columns);
    const search = toRef(props, "search");
    const {
      filteredItems
    } = useFilter(props, items, search, {
      transform: (item) => item.columns
    });
    const {
      toggleSort
    } = provideSort({
      sortBy,
      multiSort,
      mustSort,
      page
    });
    const {
      sortByWithGroups,
      opened,
      extractRows,
      isGroupOpen,
      toggleGroup
    } = provideGroupBy({
      groupBy,
      sortBy
    });
    const {
      sortedItems
    } = useSortedItems(props, filteredItems, sortByWithGroups);
    const {
      flatItems
    } = useGroupedItems(sortedItems, groupBy, opened);
    const itemsLength = computed(() => flatItems.value.length);
    const {
      startIndex,
      stopIndex,
      pageCount,
      setItemsPerPage
    } = providePagination({
      page,
      itemsPerPage,
      itemsLength
    });
    const {
      paginatedItems
    } = usePaginatedItems({
      items: flatItems,
      startIndex,
      stopIndex,
      itemsPerPage
    });
    const paginatedItemsWithoutGroups = computed(() => extractRows(paginatedItems.value));
    const {
      isSelected,
      select,
      selectAll,
      toggleSelect,
      someSelected,
      allSelected
    } = provideSelection(props, {
      allItems: items,
      currentPage: paginatedItemsWithoutGroups
    });
    const {
      isExpanded,
      toggleExpand
    } = provideExpanded(props);
    useOptions({
      page,
      itemsPerPage,
      sortBy,
      groupBy,
      search
    });
    provideDefaults({
      VDataTableRows: {
        hideNoData: toRef(props, "hideNoData"),
        noDataText: toRef(props, "noDataText"),
        loading: toRef(props, "loading"),
        loadingText: toRef(props, "loadingText")
      }
    });
    const slotProps = computed(() => ({
      page: page.value,
      itemsPerPage: itemsPerPage.value,
      sortBy: sortBy.value,
      pageCount: pageCount.value,
      toggleSort,
      setItemsPerPage,
      someSelected: someSelected.value,
      allSelected: allSelected.value,
      isSelected,
      select,
      selectAll,
      toggleSelect,
      isExpanded,
      toggleExpand,
      isGroupOpen,
      toggleGroup,
      items: paginatedItemsWithoutGroups.value,
      groupedItems: paginatedItems.value,
      columns: columns.value,
      headers: headers.value
    }));
    useRender(() => {
      const [dataTableFooterProps] = VDataTableFooter.filterProps(props);
      const [dataTableHeadersProps] = VDataTableHeaders.filterProps(props);
      const [dataTableRowsProps] = VDataTableRows.filterProps(props);
      const [tableProps] = VTable.filterProps(props);
      return createVNode(VTable, mergeProps({
        "class": ["v-data-table", {
          "v-data-table--show-select": props.showSelect,
          "v-data-table--loading": props.loading
        }, props.class],
        "style": props.style
      }, tableProps), {
        top: () => {
          var _a;
          return (_a = slots.top) == null ? void 0 : _a.call(slots, slotProps.value);
        },
        default: () => {
          var _a, _b, _c, _d;
          return slots.default ? slots.default(slotProps.value) : createVNode(Fragment, null, [(_a = slots.colgroup) == null ? void 0 : _a.call(slots, slotProps.value), createVNode("thead", null, [createVNode(VDataTableHeaders, dataTableHeadersProps, slots)]), (_b = slots.thead) == null ? void 0 : _b.call(slots, slotProps.value), createVNode("tbody", null, [slots.body ? slots.body(slotProps.value) : createVNode(VDataTableRows, mergeProps(dataTableRowsProps, {
            "items": paginatedItems.value
          }), slots)]), (_c = slots.tbody) == null ? void 0 : _c.call(slots, slotProps.value), (_d = slots.tfoot) == null ? void 0 : _d.call(slots, slotProps.value)]);
        },
        bottom: () => slots.bottom ? slots.bottom(slotProps.value) : createVNode(Fragment, null, [createVNode(VDataTableFooter, dataTableFooterProps, {
          prepend: slots["footer.prepend"]
        })])
      });
    });
    return {};
  }
});

// node_modules/vuetify/lib/labs/VDataTable/VDataTableVirtual.mjs
var makeVDataTableVirtualProps = propsFactory({
  ...makeDataTableProps(),
  ...makeDataTableGroupProps(),
  ...makeVirtualProps(),
  ...makeFilterProps()
}, "VDataTableVirtual");
var VDataTableVirtual = genericComponent()({
  name: "VDataTableVirtual",
  props: makeVDataTableVirtualProps(),
  emits: {
    "update:modelValue": (value) => true,
    "update:sortBy": (value) => true,
    "update:options": (value) => true,
    "update:groupBy": (value) => true,
    "update:expanded": (value) => true,
    "click:row": (e, value) => true
  },
  setup(props, _ref) {
    let {
      emit,
      slots
    } = _ref;
    const {
      groupBy
    } = createGroupBy(props);
    const {
      sortBy,
      multiSort,
      mustSort
    } = createSort(props);
    const {
      columns,
      headers
    } = createHeaders(props, {
      groupBy,
      showSelect: toRef(props, "showSelect"),
      showExpand: toRef(props, "showExpand")
    });
    const {
      items
    } = useDataTableItems(props, columns);
    const search = toRef(props, "search");
    const {
      filteredItems
    } = useFilter(props, items, search, {
      transform: (item) => item.columns
    });
    const {
      toggleSort
    } = provideSort({
      sortBy,
      multiSort,
      mustSort
    });
    const {
      sortByWithGroups,
      opened,
      extractRows,
      isGroupOpen,
      toggleGroup
    } = provideGroupBy({
      groupBy,
      sortBy
    });
    const {
      sortedItems
    } = useSortedItems(props, filteredItems, sortByWithGroups);
    const {
      flatItems
    } = useGroupedItems(sortedItems, groupBy, opened);
    const allItems = computed(() => extractRows(flatItems.value));
    const {
      isSelected,
      select,
      selectAll,
      toggleSelect,
      someSelected,
      allSelected
    } = provideSelection(props, {
      allItems,
      currentPage: allItems
    });
    const {
      isExpanded,
      toggleExpand
    } = provideExpanded(props);
    const headerHeight = computed(() => headers.value.length * 56);
    const {
      containerRef,
      paddingTop,
      paddingBottom,
      computedItems,
      handleItemResize,
      handleScroll
    } = useVirtual(props, flatItems, headerHeight);
    const displayItems = computed(() => computedItems.value.map((item) => item.raw));
    useOptions({
      sortBy,
      page: shallowRef(1),
      itemsPerPage: shallowRef(-1),
      groupBy,
      search
    });
    provideDefaults({
      VDataTableRows: {
        hideNoData: toRef(props, "hideNoData"),
        noDataText: toRef(props, "noDataText"),
        loading: toRef(props, "loading"),
        loadingText: toRef(props, "loadingText")
      }
    });
    const slotProps = computed(() => ({
      sortBy: sortBy.value,
      toggleSort,
      someSelected: someSelected.value,
      allSelected: allSelected.value,
      isSelected,
      select,
      selectAll,
      toggleSelect,
      isExpanded,
      toggleExpand,
      isGroupOpen,
      toggleGroup,
      items: allItems.value,
      groupedItems: flatItems.value,
      columns: columns.value,
      headers: headers.value
    }));
    useRender(() => {
      const [dataTableHeadersProps] = VDataTableHeaders.filterProps(props);
      const [dataTableRowsProps] = VDataTableRows.filterProps(props);
      const [tableProps] = VTable.filterProps(props);
      return createVNode(VTable, mergeProps({
        "class": ["v-data-table", {
          "v-data-table--loading": props.loading
        }, props.class],
        "style": props.style
      }, tableProps), {
        top: () => {
          var _a;
          return (_a = slots.top) == null ? void 0 : _a.call(slots, slotProps.value);
        },
        wrapper: () => createVNode("div", {
          "ref": containerRef,
          "onScroll": handleScroll,
          "class": "v-table__wrapper",
          "style": {
            height: convertToUnit(props.height)
          }
        }, [createVNode("table", null, [createVNode("thead", null, [createVNode(VDataTableHeaders, mergeProps(dataTableHeadersProps, {
          "sticky": props.fixedHeader
        }), slots)]), createVNode("tbody", null, [createVNode("tr", {
          "style": {
            height: convertToUnit(paddingTop.value),
            border: 0
          }
        }, [createVNode("td", {
          "colspan": columns.value.length,
          "style": {
            height: convertToUnit(paddingTop.value),
            border: 0
          }
        }, null)]), createVNode(VDataTableRows, mergeProps(dataTableRowsProps, {
          "items": displayItems.value
        }), {
          ...slots,
          item: (itemSlotProps) => createVNode(VVirtualScrollItem, {
            "key": itemSlotProps.item.index,
            "renderless": true,
            "onUpdate:height": (height) => handleItemResize(itemSlotProps.item.index, height)
          }, {
            default: (_ref2) => {
              var _a;
              let {
                itemRef
              } = _ref2;
              return ((_a = slots.item) == null ? void 0 : _a.call(slots, {
                ...itemSlotProps,
                itemRef
              })) ?? createVNode(VDataTableRow, mergeProps(itemSlotProps.props, {
                "ref": itemRef,
                "key": itemSlotProps.item.index
              }), slots);
            }
          })
        }), createVNode("tr", {
          "style": {
            height: convertToUnit(paddingBottom.value),
            border: 0
          }
        }, [createVNode("td", {
          "colspan": columns.value.length,
          "style": {
            height: convertToUnit(paddingBottom.value),
            border: 0
          }
        }, null)])])])]),
        bottom: () => {
          var _a;
          return (_a = slots.bottom) == null ? void 0 : _a.call(slots, slotProps.value);
        }
      });
    });
  }
});

// node_modules/vuetify/lib/labs/VDataTable/VDataTableServer.mjs
var makeVDataTableServerProps = propsFactory({
  itemsLength: {
    type: [Number, String],
    required: true
  },
  ...makeDataTablePaginateProps(),
  ...makeDataTableProps(),
  ...makeVDataTableFooterProps()
}, "VDataTableServer");
var VDataTableServer = genericComponent()({
  name: "VDataTableServer",
  props: makeVDataTableServerProps(),
  emits: {
    "update:modelValue": (value) => true,
    "update:page": (page) => true,
    "update:itemsPerPage": (page) => true,
    "update:sortBy": (sortBy) => true,
    "update:options": (options) => true,
    "update:expanded": (options) => true,
    "update:groupBy": (value) => true,
    "click:row": (e, value) => true
  },
  setup(props, _ref) {
    let {
      emit,
      slots
    } = _ref;
    const {
      groupBy
    } = createGroupBy(props);
    const {
      sortBy,
      multiSort,
      mustSort
    } = createSort(props);
    const {
      page,
      itemsPerPage
    } = createPagination(props);
    const itemsLength = computed(() => parseInt(props.itemsLength, 10));
    const {
      columns,
      headers
    } = createHeaders(props, {
      groupBy,
      showSelect: toRef(props, "showSelect"),
      showExpand: toRef(props, "showExpand")
    });
    const {
      items
    } = useDataTableItems(props, columns);
    const {
      toggleSort
    } = provideSort({
      sortBy,
      multiSort,
      mustSort,
      page
    });
    const {
      opened,
      isGroupOpen,
      toggleGroup,
      extractRows
    } = provideGroupBy({
      groupBy,
      sortBy
    });
    const {
      pageCount,
      setItemsPerPage
    } = providePagination({
      page,
      itemsPerPage,
      itemsLength
    });
    const {
      flatItems
    } = useGroupedItems(items, groupBy, opened);
    const {
      isSelected,
      select,
      selectAll,
      toggleSelect,
      someSelected,
      allSelected
    } = provideSelection(props, {
      allItems: items,
      currentPage: items
    });
    const {
      isExpanded,
      toggleExpand
    } = provideExpanded(props);
    const itemsWithoutGroups = computed(() => extractRows(items.value));
    useOptions({
      page,
      itemsPerPage,
      sortBy,
      groupBy,
      search: toRef(props, "search")
    });
    provide("v-data-table", {
      toggleSort,
      sortBy
    });
    provideDefaults({
      VDataTableRows: {
        hideNoData: toRef(props, "hideNoData"),
        noDataText: toRef(props, "noDataText"),
        loading: toRef(props, "loading"),
        loadingText: toRef(props, "loadingText")
      }
    });
    const slotProps = computed(() => ({
      page: page.value,
      itemsPerPage: itemsPerPage.value,
      sortBy: sortBy.value,
      pageCount: pageCount.value,
      toggleSort,
      setItemsPerPage,
      someSelected: someSelected.value,
      allSelected: allSelected.value,
      isSelected,
      select,
      selectAll,
      toggleSelect,
      isExpanded,
      toggleExpand,
      isGroupOpen,
      toggleGroup,
      items: itemsWithoutGroups.value,
      groupedItems: flatItems.value,
      columns: columns.value,
      headers: headers.value
    }));
    useRender(() => {
      const [dataTableFooterProps] = VDataTableFooter.filterProps(props);
      const [dataTableHeadersProps] = VDataTableHeaders.filterProps(props);
      const [dataTableRowsProps] = VDataTableRows.filterProps(props);
      const [tableProps] = VTable.filterProps(props);
      return createVNode(VTable, mergeProps({
        "class": ["v-data-table", {
          "v-data-table--loading": props.loading
        }, props.class],
        "style": props.style
      }, tableProps), {
        top: () => {
          var _a;
          return (_a = slots.top) == null ? void 0 : _a.call(slots, slotProps.value);
        },
        default: () => {
          var _a, _b, _c, _d;
          return slots.default ? slots.default(slotProps.value) : createVNode(Fragment, null, [(_a = slots.colgroup) == null ? void 0 : _a.call(slots, slotProps.value), createVNode("thead", {
            "class": "v-data-table__thead",
            "role": "rowgroup"
          }, [createVNode(VDataTableHeaders, mergeProps(dataTableHeadersProps, {
            "sticky": props.fixedHeader
          }), slots)]), (_b = slots.thead) == null ? void 0 : _b.call(slots, slotProps.value), createVNode("tbody", {
            "class": "v-data-table__tbody",
            "role": "rowgroup"
          }, [slots.body ? slots.body(slotProps.value) : createVNode(VDataTableRows, mergeProps(dataTableRowsProps, {
            "items": flatItems.value
          }), slots)]), (_c = slots.tbody) == null ? void 0 : _c.call(slots, slotProps.value), (_d = slots.tfoot) == null ? void 0 : _d.call(slots, slotProps.value)]);
        },
        bottom: () => slots.bottom ? slots.bottom(slotProps.value) : createVNode(VDataTableFooter, dataTableFooterProps, {
          prepend: slots["footer.prepend"]
        })
      });
    });
  }
});

// node_modules/vuetify/lib/labs/VDatePicker/VDateCard.mjs
import "E:/Project/CyberNomads/cyber-nomads-register-ui/node_modules/vuetify/lib/labs/VDatePicker/VDateCard.css";

// node_modules/vuetify/lib/labs/VDatePicker/VDatePickerControls.mjs
import "E:/Project/CyberNomads/cyber-nomads-register-ui/node_modules/vuetify/lib/labs/VDatePicker/VDatePickerControls.css";

// node_modules/vuetify/lib/labs/VDateInput/composables.mjs
var makeDateProps2 = propsFactory({
  modelValue: {
    type: null,
    default: () => []
  },
  displayDate: {
    type: null,
    default: null
  },
  inputMode: {
    type: String,
    default: "calendar"
  },
  viewMode: {
    type: String,
    default: "month"
  },
  format: String
}, "date");
var dateEmits = {
  "update:modelValue": (date) => true,
  "update:displayDate": (date) => true,
  "update:focused": (focused) => true,
  "update:inputMode": (inputMode) => true,
  "update:viewMode": (viewMode) => true
};
function createDateInput(props, isRange) {
  const adapter = useDate();
  const model = useProxiedModel(props, "modelValue", [], (v) => {
    if (v == null) return [];
    const arr = wrapInArray(v).filter((v2) => !!v2);
    return arr.map(adapter.date);
  }, (v) => {
    const arr = wrapInArray(v);
    const formatted = props.format ? arr.map((d) => adapter.format(d, props.format)) : arr;
    if (isRange) return formatted;
    return formatted[0];
  });
  const inputMode = useProxiedModel(props, "inputMode");
  const viewMode = useProxiedModel(props, "viewMode");
  const displayDate = useProxiedModel(props, "displayDate", model.value.length ? model.value[0] : adapter.date());
  function parseKeyboardDate(input, fallback) {
    const date = adapter.date(input);
    return adapter.isValid(date) ? date : fallback;
  }
  function isEqual(model2, comparing) {
    if (model2.length !== comparing.length) return false;
    for (let i = 0; i < model2.length; i++) {
      if (comparing[i] && !adapter.isEqual(model2[i], comparing[i])) {
        return false;
      }
    }
    return true;
  }
  return {
    model,
    adapter,
    inputMode,
    viewMode,
    displayDate,
    parseKeyboardDate,
    isEqual
  };
}

// node_modules/vuetify/lib/labs/VDatePicker/VDatePickerControls.mjs
var makeVDatePickerControlsProps = propsFactory({
  nextIcon: {
    type: [String],
    default: "$next"
  },
  prevIcon: {
    type: [String],
    default: "$prev"
  },
  expandIcon: {
    type: [String],
    default: "$expand"
  },
  collapseIcon: {
    type: [String],
    default: "$collapse"
  },
  range: {
    default: false,
    type: [Boolean, String],
    validator: (v) => v === false || ["start", "end"].includes(v)
  },
  ...omit(makeDateProps2(), ["modelValue", "inputMode"])
}, "VDatePickerControls");
var VDatePickerControls = genericComponent()({
  name: "VDatePickerControls",
  props: makeVDatePickerControlsProps(),
  emits: {
    ...omit(dateEmits, ["update:modelValue", "update:inputMode"])
  },
  setup(props, _ref) {
    let {
      emit
    } = _ref;
    const adapter = useDate();
    const monthAndYear = computed(() => {
      const month = props.range === "end" ? adapter.addMonths(props.displayDate, 1) : props.displayDate;
      return adapter.format(month, "monthAndYear");
    });
    useRender(() => {
      const prevBtn = createVNode(VBtn, {
        "variant": "text",
        "icon": props.prevIcon,
        "onClick": () => emit("update:displayDate", adapter.addMonths(props.displayDate, -1))
      }, null);
      const nextBtn = createVNode(VBtn, {
        "variant": "text",
        "icon": props.nextIcon,
        "onClick": () => emit("update:displayDate", adapter.addMonths(props.displayDate, 1))
      }, null);
      return createVNode("div", {
        "class": "v-date-picker-controls"
      }, [props.viewMode === "month" && props.range === "start" && prevBtn, !!props.range && createVNode(VSpacer, {
        "key": "range-spacer"
      }, null), createVNode("div", {
        "class": "v-date-picker-controls__date"
      }, [monthAndYear.value]), createVNode(VBtn, {
        "key": "expand-btn",
        "variant": "text",
        "icon": props.viewMode === "month" ? props.expandIcon : props.collapseIcon,
        "onClick": () => emit("update:viewMode", props.viewMode === "month" ? "year" : "month")
      }, null), createVNode(VSpacer, null, null), props.viewMode === "month" && !props.range && createVNode("div", {
        "class": "v-date-picker-controls__month",
        "key": "month-buttons"
      }, [prevBtn, nextBtn]), props.viewMode === "month" && props.range === "end" && nextBtn]);
    });
    return {};
  }
});

// node_modules/vuetify/lib/labs/VDatePicker/VDatePickerMonth.mjs
import "E:/Project/CyberNomads/cyber-nomads-register-ui/node_modules/vuetify/lib/labs/VDatePicker/VDatePickerMonth.css";

// node_modules/vuetify/lib/labs/VDatePicker/composables.mjs
var DatePickerSymbol = Symbol.for("vuetify:date-picker");
function createDatePicker(props) {
  const hoverDate = ref();
  const hoverMonth = ref();
  const isDragging = ref(false);
  const dragHandle = ref(null);
  const hasScrolled = ref(false);
  provide(DatePickerSymbol, {
    hoverDate,
    hoverMonth,
    isDragging,
    dragHandle,
    hasScrolled
  });
  const {
    model,
    displayDate,
    viewMode,
    inputMode,
    isEqual
  } = createDateInput(props, !!props.multiple);
  return {
    hoverDate,
    hoverMonth,
    isDragging,
    dragHandle,
    hasScrolled,
    model,
    displayDate,
    viewMode,
    inputMode,
    isEqual
  };
}
function useDatePicker() {
  const datePicker = inject(DatePickerSymbol);
  if (!datePicker) throw new Error("foo");
  return datePicker;
}

// node_modules/vuetify/lib/labs/VDatePicker/VDatePickerMonth.mjs
var makeVDatePickerMonthProps = propsFactory({
  color: String,
  showAdjacentMonths: Boolean,
  hideWeekdays: Boolean,
  showWeek: Boolean,
  hoverDate: null,
  multiple: Boolean,
  side: {
    type: String
  },
  ...omit(makeDateProps2(), ["inputMode", "viewMode"])
}, "VDatePickerMonth");
var VDatePickerMonth = genericComponent()({
  name: "VDatePickerMonth",
  props: makeVDatePickerMonthProps({
    color: "surface-variant"
  }),
  emits: {
    ...omit(dateEmits, ["update:inputMode", "update:viewMode"]),
    "update:hoverDate": (date) => true
  },
  setup(props, _ref) {
    let {
      emit,
      slots
    } = _ref;
    const adapter = useDate();
    const {
      isDragging,
      dragHandle,
      hasScrolled
    } = useDatePicker();
    const month = computed(() => props.displayDate);
    const findClosestDate = (date, dates) => {
      const {
        isSameDay,
        getDiff
      } = adapter;
      const [startDate, endDate] = dates;
      if (isSameDay(startDate, endDate)) {
        return getDiff(date, startDate, "days") > 0 ? endDate : startDate;
      }
      const distStart = Math.abs(getDiff(date, startDate));
      const distEnd = Math.abs(getDiff(date, endDate));
      return distStart < distEnd ? startDate : endDate;
    };
    const weeksInMonth = computed(() => {
      const weeks2 = adapter.getWeekArray(month.value);
      const days = weeks2.flat();
      const daysInMonth2 = 6 * 7;
      if (days.length < daysInMonth2 && props.showAdjacentMonths) {
        const lastDay = days[days.length - 1];
        let week = [];
        for (let day = 1; day <= daysInMonth2 - days.length; day++) {
          week.push(adapter.addDays(lastDay, day));
          if (day % 7 === 0) {
            weeks2.push(week);
            week = [];
          }
        }
      }
      return weeks2;
    });
    const daysInMonth = computed(() => {
      const validDates = props.modelValue.filter((v) => !!v);
      const isRange = validDates.length > 1;
      const days = weeksInMonth.value.flat();
      const today = adapter.date();
      const startDate = validDates[0];
      const endDate = validDates[1];
      return days.map((date, index) => {
        const isStart = startDate && adapter.isSameDay(date, startDate);
        const isEnd = endDate && adapter.isSameDay(date, endDate);
        const isAdjacent = !adapter.isSameMonth(date, month.value);
        const isSame = validDates.length === 2 && adapter.isSameDay(startDate, endDate);
        return {
          date,
          isoDate: toIso(adapter, date),
          formatted: adapter.format(date, "keyboardDate"),
          year: adapter.getYear(date),
          month: adapter.getMonth(date),
          isWeekStart: index % 7 === 0,
          isWeekEnd: index % 7 === 6,
          isSelected: isStart || isEnd,
          isStart,
          isEnd,
          isToday: adapter.isSameDay(date, today),
          isAdjacent,
          isHidden: isAdjacent && !props.showAdjacentMonths,
          inRange: isRange && !isSame && (isStart || isEnd || validDates.length === 2 && adapter.isWithinRange(date, validDates)),
          // isHovered: props.hoverDate === date,
          // inHover: hoverRange.value && isWithinRange(date, hoverRange.value),
          isHovered: false,
          inHover: false,
          localized: adapter.format(date, "dayOfMonth")
        };
      });
    });
    const weeks = computed(() => {
      return weeksInMonth.value.map((week) => {
        return getWeek(adapter, week[0]);
      });
    });
    const {
      backgroundColorClasses,
      backgroundColorStyles
    } = useBackgroundColor(props, "color");
    function selectDate(date) {
      let newModel = props.modelValue.slice();
      if (props.multiple) {
        if (isDragging.value && dragHandle.value != null) {
          const otherIndex = (dragHandle.value + 1) % 2;
          const fn = otherIndex === 0 ? "isBefore" : "isAfter";
          if (adapter[fn](date, newModel[otherIndex])) {
            newModel[dragHandle.value] = newModel[otherIndex];
            newModel[otherIndex] = date;
            dragHandle.value = otherIndex;
          } else {
            newModel[dragHandle.value] = date;
          }
        } else {
          if (newModel.find((d) => adapter.isSameDay(d, date))) {
            newModel = newModel.filter((v) => !adapter.isSameDay(v, date));
          } else if (newModel.length === 2) {
            let index;
            if (!props.side || adapter.isSameMonth(newModel[0], newModel[1])) {
              const closest = findClosestDate(date, newModel);
              index = newModel.indexOf(closest);
            } else {
              index = props.side === "start" ? 0 : props.side === "end" ? 1 : void 0;
            }
            newModel = newModel.map((v, i) => i === index ? date : v);
          } else {
            if (newModel[0] && adapter.isBefore(newModel[0], date)) {
              newModel = [newModel[0], date];
            } else {
              newModel = [date, newModel[0]];
            }
          }
        }
      } else {
        newModel = [date];
      }
      emit("update:modelValue", newModel.filter((v) => !!v));
    }
    const daysRef = ref();
    function findElement(el) {
      if (!el || el === daysRef.value) return null;
      if ("vDate" in el.dataset) {
        return adapter.date(el.dataset.vDate);
      }
      return findElement(el.parentElement);
    }
    function findDate(e) {
      var _a, _b;
      const x = "changedTouches" in e ? (_a = e.changedTouches[0]) == null ? void 0 : _a.clientX : e.clientX;
      const y = "changedTouches" in e ? (_b = e.changedTouches[0]) == null ? void 0 : _b.clientY : e.clientY;
      const el = document.elementFromPoint(x, y);
      return findElement(el);
    }
    let canDrag = false;
    function handleMousedown(e) {
      hasScrolled.value = false;
      const selected = findDate(e);
      if (!selected) return;
      const modelIndex = props.modelValue.findIndex((d) => adapter.isEqual(d, selected));
      if (modelIndex >= 0) {
        canDrag = true;
        dragHandle.value = modelIndex;
        window.addEventListener("touchmove", handleTouchmove, {
          passive: false
        });
        window.addEventListener("mousemove", handleTouchmove, {
          passive: false
        });
        e.preventDefault();
      }
      window.addEventListener("touchend", handleTouchend, {
        passive: false
      });
      window.addEventListener("mouseup", handleTouchend, {
        passive: false
      });
    }
    function handleTouchmove(e) {
      if (!canDrag) return;
      e.preventDefault();
      isDragging.value = true;
      const over = findDate(e);
      if (!over) return;
      selectDate(over);
    }
    function handleTouchend(e) {
      if (e.cancelable) e.preventDefault();
      window.removeEventListener("touchmove", handleTouchmove);
      window.removeEventListener("mousemove", handleTouchmove);
      window.removeEventListener("touchend", handleTouchend);
      window.removeEventListener("mouseup", handleTouchend);
      const end = findDate(e);
      if (!end) return;
      if (!hasScrolled.value) {
        selectDate(end);
      }
      isDragging.value = false;
      dragHandle.value = null;
      canDrag = false;
    }
    return () => createVNode("div", {
      "class": "v-date-picker-month"
    }, [props.showWeek && createVNode("div", {
      "key": "weeks",
      "class": "v-date-picker-month__weeks"
    }, [!props.hideWeekdays && createVNode("div", {
      "key": "hide-week-days",
      "class": "v-date-picker-month__day"
    }, [createTextVNode(" ")]), weeks.value.map((week) => createVNode("div", {
      "class": ["v-date-picker-month__day", "v-date-picker-month__day--adjacent"]
    }, [week]))]), createVNode("div", {
      "ref": daysRef,
      "class": "v-date-picker-month__days",
      "onMousedown": handleMousedown,
      "onTouchstart": handleMousedown
    }, [!props.hideWeekdays && adapter.getWeekdays().map((weekDay) => createVNode("div", {
      "class": ["v-date-picker-month__day", "v-date-picker-month__weekday"]
    }, [weekDay])), daysInMonth.value.map((item, index) => createVNode("div", {
      "class": ["v-date-picker-month__day", {
        "v-date-picker-month__day--selected": item.isSelected,
        "v-date-picker-month__day--start": item.isStart,
        "v-date-picker-month__day--end": item.isEnd,
        "v-date-picker-month__day--adjacent": item.isAdjacent,
        "v-date-picker-month__day--hide-adjacent": item.isHidden,
        "v-date-picker-month__day--week-start": item.isWeekStart,
        "v-date-picker-month__day--week-end": item.isWeekEnd,
        "v-date-picker-month__day--hovered": item.isHovered
      }],
      "data-v-date": !item.isHidden ? item.isoDate : void 0
    }, [item.inRange && createVNode("div", {
      "key": "in-range",
      "class": ["v-date-picker-month__day--range", backgroundColorClasses.value],
      "style": backgroundColorStyles.value
    }, null), item.inHover && !item.isStart && !item.isEnd && !item.isHovered && !item.inRange && createVNode("div", {
      "key": "in-hover",
      "class": "v-date-picker-month__day--hover"
    }, null), (props.showAdjacentMonths || !item.isAdjacent) && createVNode(VBtn, {
      "icon": true,
      "ripple": false,
      "variant": (item.isToday || item.isHovered) && !item.isSelected ? "outlined" : "flat",
      "active": item.isSelected,
      "color": item.isSelected || item.isToday ? props.color : item.isHovered ? void 0 : "transparent"
    }, {
      default: () => [item.localized]
    })]))])]);
  }
});

// node_modules/vuetify/lib/labs/VDatePicker/VDatePickerYears.mjs
import "E:/Project/CyberNomads/cyber-nomads-register-ui/node_modules/vuetify/lib/labs/VDatePicker/VDatePickerYears.css";
var makeVDatePickerYearsProps = propsFactory({
  color: String,
  min: Number,
  max: Number,
  height: [String, Number],
  displayDate: null
}, "VDatePickerYears");
var VDatePickerYears = genericComponent()({
  name: "VDatePickerYears",
  props: makeVDatePickerYearsProps(),
  emits: {
    "update:displayDate": (date) => true,
    "update:viewMode": (date) => true
  },
  setup(props, _ref) {
    let {
      emit
    } = _ref;
    const adapter = useDate();
    const displayYear = computed(() => adapter.getYear(props.displayDate ?? /* @__PURE__ */ new Date()));
    const years = computed(() => {
      const min = props.min ?? displayYear.value - 50 - 2;
      const max = props.max ?? displayYear.value + 50;
      return createRange(max - min, min);
    });
    const yearRef = ref();
    onMounted(() => {
      var _a;
      (_a = yearRef.value) == null ? void 0 : _a.$el.scrollIntoView({
        block: "center"
      });
    });
    useRender(() => createVNode("div", {
      "class": "v-date-picker-years",
      "style": {
        height: convertToUnit(props.height)
      }
    }, [createVNode("div", {
      "class": "v-date-picker-years__content"
    }, [years.value.map((year) => createVNode(VBtn, {
      "ref": year === displayYear.value ? yearRef : void 0,
      "variant": year === displayYear.value ? "flat" : "text",
      "rounded": "xl",
      "active": year === displayYear.value,
      "color": year === displayYear.value ? props.color : void 0,
      "onClick": () => {
        emit("update:displayDate", adapter.setYear(props.displayDate, year));
        emit("update:viewMode", "month");
      }
    }, {
      default: () => [year]
    }))])]));
    return {};
  }
});

// node_modules/vuetify/lib/labs/VDatePicker/VDateCard.mjs
var makeVDateCardProps = propsFactory({
  cancelText: {
    type: String,
    default: "$vuetify.datePicker.cancel"
  },
  okText: {
    type: String,
    default: "$vuetify.datePicker.ok"
  },
  inputMode: {
    type: String,
    default: "calendar"
  },
  hideActions: Boolean,
  ...makeVDatePickerControlsProps(),
  ...makeVDatePickerMonthProps(),
  ...makeVDatePickerYearsProps(),
  ...makeTransitionProps({
    transition: {
      component: VFadeTransition,
      leaveAbsolute: true
    }
  })
}, "VDateCard");
var VDateCard = genericComponent()({
  name: "VDateCard",
  props: makeVDateCardProps(),
  emits: {
    save: () => true,
    cancel: () => true,
    "update:displayDate": (value) => true,
    "update:inputMode": (value) => true,
    "update:modelValue": (value) => true,
    "update:viewMode": (mode) => true
  },
  setup(props, _ref) {
    let {
      emit,
      slots
    } = _ref;
    const model = useProxiedModel(props, "modelValue");
    const {
      t
    } = useLocale();
    createDatePicker(props);
    function onDisplayUpdate(val) {
      emit("update:displayDate", val);
    }
    function onViewModeUpdate(val) {
      emit("update:viewMode", val);
    }
    function onSave() {
      emit("update:modelValue", model.value);
      emit("save");
    }
    function onCancel() {
      emit("cancel");
    }
    useRender(() => {
      const [cardProps] = VCard.filterProps(props);
      const [datePickerControlsProps] = VDatePickerControls.filterProps(props);
      const [datePickerMonthProps] = VDatePickerMonth.filterProps(props);
      const [datePickerYearsProps] = VDatePickerYears.filterProps(props);
      const hasActions = !props.hideActions || !!slots.actions;
      return createVNode(VCard, mergeProps(cardProps, {
        "class": "v-date-card"
      }), {
        ...slots,
        default: () => createVNode(Fragment, null, [createVNode(VDatePickerControls, mergeProps(datePickerControlsProps, {
          "onUpdate:displayDate": onDisplayUpdate,
          "onUpdate:viewMode": onViewModeUpdate
        }), null), createVNode(MaybeTransition, {
          "transition": props.transition
        }, {
          default: () => [props.viewMode === "month" ? createVNode(VDatePickerMonth, mergeProps(datePickerMonthProps, {
            "modelValue": model.value,
            "onUpdate:modelValue": ($event) => model.value = $event,
            "onUpdate:displayDate": onDisplayUpdate
          }), null) : createVNode(VDatePickerYears, mergeProps(datePickerYearsProps, {
            "onUpdate:displayDate": onDisplayUpdate,
            "onUpdate:viewMode": onViewModeUpdate
          }), null)]
        })]),
        actions: !hasActions ? void 0 : () => {
          var _a;
          return createVNode(Fragment, null, [((_a = slots.actions) == null ? void 0 : _a.call(slots)) ?? createVNode(Fragment, null, [createVNode(VBtn, {
            "onClick": onCancel,
            "text": t(props.cancelText)
          }, null), createVNode(VBtn, {
            "onClick": onSave,
            "text": t(props.okText)
          }, null)])]);
        }
      });
    });
    return {};
  }
});

// node_modules/vuetify/lib/labs/VDatePicker/VDatePicker.mjs
import "E:/Project/CyberNomads/cyber-nomads-register-ui/node_modules/vuetify/lib/labs/VDatePicker/VDatePicker.css";

// node_modules/vuetify/lib/labs/VDatePicker/VDatePickerHeader.mjs
import "E:/Project/CyberNomads/cyber-nomads-register-ui/node_modules/vuetify/lib/labs/VDatePicker/VDatePickerHeader.css";
var makeVDatePickerHeaderProps = propsFactory({
  appendIcon: String,
  color: String,
  header: String,
  transition: String
}, "VDatePickerHeader");
var VDatePickerHeader = genericComponent()({
  name: "VDatePickerHeader",
  props: makeVDatePickerHeaderProps(),
  emits: {
    "click:append": () => true
  },
  setup(props, _ref) {
    let {
      emit,
      slots
    } = _ref;
    const {
      backgroundColorClasses,
      backgroundColorStyles
    } = useBackgroundColor(props, "color");
    function onClickAppend() {
      emit("click:append");
    }
    useRender(() => {
      const hasContent = !!(slots.default || props.header);
      const hasAppend = !!(slots.append || props.appendIcon);
      return createVNode("div", {
        "class": ["v-date-picker-header", backgroundColorClasses.value],
        "style": backgroundColorStyles.value
      }, [slots.prepend && createVNode("div", {
        "key": "prepend",
        "class": "v-date-picker-header__prepend"
      }, [slots.prepend()]), hasContent && createVNode(MaybeTransition, {
        "key": "content",
        "name": props.transition
      }, {
        default: () => {
          var _a;
          return [createVNode("div", {
            "key": props.header,
            "class": "v-date-picker-header__content"
          }, [((_a = slots.default) == null ? void 0 : _a.call(slots)) ?? props.header])];
        }
      }), hasAppend && createVNode("div", {
        "class": "v-date-picker-header__append"
      }, [!slots.append ? createVNode(VBtn, {
        "key": "append-btn",
        "icon": props.appendIcon,
        "variant": "text",
        "onClick": onClickAppend
      }, null) : createVNode(VDefaultsProvider, {
        "key": "append-defaults",
        "disabled": !props.appendIcon,
        "defaults": {
          VBtn: {
            icon: props.appendIcon,
            variant: "text"
          }
        }
      }, {
        default: () => {
          var _a;
          return [(_a = slots.append) == null ? void 0 : _a.call(slots)];
        }
      })])]);
    });
    return {};
  }
});

// node_modules/vuetify/lib/labs/VPicker/VPicker.mjs
import "E:/Project/CyberNomads/cyber-nomads-register-ui/node_modules/vuetify/lib/labs/VPicker/VPicker.css";

// node_modules/vuetify/lib/labs/VPicker/VPickerTitle.mjs
var VPickerTitle = createSimpleFunctional("v-picker-title");

// node_modules/vuetify/lib/labs/VPicker/VPicker.mjs
var makeVPickerProps = propsFactory({
  landscape: Boolean,
  title: String,
  ...omit(makeVSheetProps(), ["color"])
}, "VPicker");
var VPicker = genericComponent()({
  name: "VPicker",
  props: makeVPickerProps(),
  setup(props, _ref) {
    let {
      slots
    } = _ref;
    useRender(() => {
      const [sheetProps] = VSheet.filterProps(props);
      const hasTitle = !!(props.title || slots.title);
      return createVNode(VSheet, mergeProps(sheetProps, {
        "class": ["v-picker", {
          "v-picker--landscape": props.landscape,
          "v-picker--with-actions": !!slots.actions
        }, props.class],
        "style": props.style
      }), {
        default: () => {
          var _a, _b, _c;
          return [hasTitle && createVNode(VPickerTitle, {
            "key": "picker-title"
          }, {
            default: () => {
              var _a2;
              return [((_a2 = slots.title) == null ? void 0 : _a2.call(slots)) ?? props.title];
            }
          }), slots.header && createVNode("div", {
            "class": "v-picker__header"
          }, [slots.header()]), createVNode("div", {
            "class": "v-picker__body"
          }, [(_a = slots.default) == null ? void 0 : _a.call(slots)]), ((_c = (_b = slots.actions) == null ? void 0 : _b.call(slots)[0]) == null ? void 0 : _c.children) && createVNode("div", {
            "class": "v-picker__actions"
          }, [slots.actions()])];
        }
      });
    });
    return {};
  }
});

// node_modules/vuetify/lib/labs/VDatePicker/VDatePicker.mjs
var makeVDatePickerProps = propsFactory({
  calendarIcon: {
    type: String,
    default: "$calendar"
  },
  keyboardIcon: {
    type: String,
    default: "$edit"
  },
  cancelText: {
    type: String,
    default: "$vuetify.datePicker.cancel"
  },
  okText: {
    type: String,
    default: "$vuetify.datePicker.ok"
  },
  inputText: {
    type: String,
    default: "$vuetify.datePicker.input.placeholder"
  },
  inputPlaceholder: {
    type: String,
    default: "dd/mm/yyyy"
  },
  header: {
    type: String,
    default: "$vuetify.datePicker.header"
  },
  hideActions: Boolean,
  ...makeDateProps2(),
  ...makeVDatePickerControlsProps(),
  ...makeVDatePickerMonthProps(),
  ...makeVDatePickerYearsProps(),
  ...makeVPickerProps({
    title: "$vuetify.datePicker.title"
  })
}, "VDatePicker");
var VDatePicker = genericComponent()({
  name: "VDatePicker",
  props: makeVDatePickerProps(),
  emits: {
    "click:cancel": () => true,
    "click:save": () => true,
    ...dateEmits
  },
  setup(props, _ref) {
    let {
      emit,
      slots
    } = _ref;
    const adapter = useDate();
    const {
      t
    } = useLocale();
    const {
      model,
      displayDate,
      viewMode,
      inputMode,
      isEqual
    } = createDatePicker(props);
    const isReversing = shallowRef(false);
    const inputModel = ref(model.value.map((date) => adapter.format(date, "keyboardDate")));
    const temporaryModel = ref(model.value);
    const title = computed(() => t(props.title));
    const header = computed(() => model.value.length ? adapter.format(model.value[0], "normalDateWithWeekday") : t(props.header));
    const headerIcon = computed(() => inputMode.value === "calendar" ? props.keyboardIcon : props.calendarIcon);
    const headerTransition = computed(() => `date-picker-header${isReversing.value ? "-reverse" : ""}-transition`);
    function updateFromInput(input, index) {
      const {
        isValid,
        date
      } = adapter;
      if (isValid(input)) {
        const newModel = model.value.slice();
        newModel[index] = date(input);
        if (props.hideActions) {
          model.value = newModel;
        } else {
          temporaryModel.value = newModel;
        }
      }
    }
    watch(model, (val) => {
      if (!isEqual(val, temporaryModel.value)) {
        temporaryModel.value = val;
      }
      inputModel.value = val.map((date) => adapter.format(date, "keyboardDate"));
    });
    watch(temporaryModel, (val, oldVal) => {
      if (props.hideActions && !isEqual(val, model.value)) {
        model.value = val;
      }
      if (val[0] && oldVal[0]) {
        isReversing.value = adapter.isBefore(val[0], oldVal[0]);
      }
    });
    function onClickCancel() {
      emit("click:cancel");
    }
    function onClickSave() {
      emit("click:save");
      model.value = temporaryModel.value;
    }
    function onClickAppend() {
      inputMode.value = inputMode.value === "calendar" ? "keyboard" : "calendar";
    }
    const headerSlotProps = computed(() => ({
      header: header.value,
      appendIcon: headerIcon.value,
      transition: headerTransition.value,
      "onClick:append": onClickAppend
    }));
    useRender(() => {
      const [pickerProps] = VPicker.filterProps(props);
      const [datePickerControlsProps] = VDatePickerControls.filterProps(props);
      const [datePickerMonthProps] = VDatePickerMonth.filterProps(props);
      const [datePickerYearsProps] = VDatePickerYears.filterProps(props);
      return createVNode(VPicker, mergeProps(pickerProps, {
        "class": ["v-date-picker", props.class],
        "style": props.style,
        "title": title.value,
        "width": props.showWeek ? 408 : 360
      }), {
        header: () => {
          var _a;
          return ((_a = slots.header) == null ? void 0 : _a.call(slots, headerSlotProps.value)) ?? createVNode(VDatePickerHeader, mergeProps({
            "key": "header"
          }, headerSlotProps.value), null);
        },
        default: () => inputMode.value === "calendar" ? createVNode(Fragment, null, [createVNode(VDatePickerControls, mergeProps(datePickerControlsProps, {
          "displayDate": displayDate.value,
          "onUpdate:displayDate": ($event) => displayDate.value = $event,
          "viewMode": viewMode.value,
          "onUpdate:viewMode": ($event) => viewMode.value = $event
        }), null), createVNode(VFadeTransition, {
          "hideOnLeave": true
        }, {
          default: () => [viewMode.value === "month" ? createVNode(VDatePickerMonth, mergeProps({
            "key": "date-picker-month"
          }, datePickerMonthProps, {
            "modelValue": temporaryModel.value,
            "onUpdate:modelValue": ($event) => temporaryModel.value = $event,
            "displayDate": displayDate.value,
            "onUpdate:displayDate": ($event) => displayDate.value = $event
          }), null) : createVNode(VDatePickerYears, mergeProps({
            "key": "date-picker-years"
          }, datePickerYearsProps, {
            "displayDate": displayDate.value,
            "onUpdate:displayDate": ($event) => displayDate.value = $event,
            "viewMode": viewMode.value,
            "onUpdate:viewMode": ($event) => viewMode.value = $event
          }), null)]
        })]) : createVNode("div", {
          "class": "v-date-picker__input"
        }, [createVNode(VTextField, {
          "modelValue": inputModel.value[0],
          "onUpdate:modelValue": (v) => updateFromInput(v, 0),
          "label": t(props.inputText),
          "placeholder": props.inputPlaceholder
        }, null)]),
        actions: () => !props.hideActions ? createVNode("div", null, [createVNode(VBtn, {
          "variant": "text",
          "color": props.color,
          "onClick": onClickCancel,
          "text": t(props.cancelText)
        }, null), createVNode(VBtn, {
          "variant": "text",
          "color": props.color,
          "onClick": onClickSave,
          "text": t(props.okText)
        }, null)]) : void 0
      });
    });
    return {};
  }
});

// node_modules/vuetify/lib/labs/VInfiniteScroll/VInfiniteScroll.mjs
import "E:/Project/CyberNomads/cyber-nomads-register-ui/node_modules/vuetify/lib/labs/VInfiniteScroll/VInfiniteScroll.css";
var makeVInfiniteScrollProps = propsFactory({
  color: String,
  direction: {
    type: String,
    default: "vertical",
    validator: (v) => ["vertical", "horizontal"].includes(v)
  },
  side: {
    type: String,
    default: "end",
    validator: (v) => ["start", "end", "both"].includes(v)
  },
  mode: {
    type: String,
    default: "intersect",
    validator: (v) => ["intersect", "manual"].includes(v)
  },
  margin: [Number, String],
  loadMoreText: {
    type: String,
    default: "$vuetify.infiniteScroll.loadMore"
  },
  emptyText: {
    type: String,
    default: "$vuetify.infiniteScroll.empty"
  },
  ...makeDimensionProps(),
  ...makeTagProps()
}, "VInfiniteScroll");
var VInfiniteScrollIntersect = defineComponent({
  name: "VInfiniteScrollIntersect",
  props: {
    side: {
      type: String,
      required: true
    },
    rootRef: null,
    rootMargin: String
  },
  emits: {
    intersect: (side, isIntersecting) => true
  },
  setup(props, _ref) {
    let {
      emit
    } = _ref;
    const {
      intersectionRef,
      isIntersecting
    } = useIntersectionObserver((entries) => {
    }, props.rootMargin ? {
      rootMargin: props.rootMargin
    } : void 0);
    watch(isIntersecting, async (val) => {
      emit("intersect", props.side, val);
    });
    useRender(() => createVNode("div", {
      "class": "v-infinite-scroll-intersect",
      "ref": intersectionRef
    }, [createTextVNode(" ")]));
    return {};
  }
});
var VInfiniteScroll = genericComponent()({
  name: "VInfiniteScroll",
  props: makeVInfiniteScrollProps(),
  emits: {
    load: (options) => true
  },
  setup(props, _ref2) {
    let {
      slots,
      emit
    } = _ref2;
    const rootEl = ref();
    const startStatus = shallowRef("ok");
    const endStatus = shallowRef("ok");
    const margin = computed(() => convertToUnit(props.margin));
    const isIntersecting = shallowRef(false);
    function setScrollAmount(amount) {
      if (!rootEl.value) return;
      const property = props.direction === "vertical" ? "scrollTop" : "scrollLeft";
      rootEl.value[property] = amount;
    }
    function getScrollAmount() {
      if (!rootEl.value) return 0;
      const property = props.direction === "vertical" ? "scrollTop" : "scrollLeft";
      return rootEl.value[property];
    }
    function getScrollSize() {
      if (!rootEl.value) return 0;
      const property = props.direction === "vertical" ? "scrollHeight" : "scrollWidth";
      return rootEl.value[property];
    }
    function getContainerSize() {
      if (!rootEl.value) return 0;
      const property = props.direction === "vertical" ? "clientHeight" : "clientWidth";
      return rootEl.value[property];
    }
    onMounted(() => {
      if (!rootEl.value) return;
      if (props.side === "start") {
        setScrollAmount(getScrollSize());
      } else if (props.side === "both") {
        setScrollAmount(getScrollSize() / 2 - getContainerSize() / 2);
      }
    });
    function setStatus(side, status) {
      if (side === "start") {
        startStatus.value = status;
      } else if (side === "end") {
        endStatus.value = status;
      }
    }
    function getStatus(side) {
      return side === "start" ? startStatus.value : endStatus.value;
    }
    let previousScrollSize = 0;
    function handleIntersect(side, _isIntersecting) {
      isIntersecting.value = _isIntersecting;
      if (isIntersecting.value) {
        intersecting(side);
      }
    }
    function intersecting(side) {
      if (props.mode !== "manual" && !isIntersecting.value) return;
      const status = getStatus(side);
      if (!rootEl.value || status === "loading") return;
      previousScrollSize = getScrollSize();
      setStatus(side, "loading");
      function done(status2) {
        setStatus(side, status2);
        nextTick(() => {
          if (status2 === "empty" || status2 === "error") return;
          if (status2 === "ok" && side === "start") {
            setScrollAmount(getScrollSize() - previousScrollSize + getScrollAmount());
          }
          if (props.mode !== "manual") {
            nextTick(() => {
              window.requestAnimationFrame(() => {
                window.requestAnimationFrame(() => {
                  window.requestAnimationFrame(() => {
                    intersecting(side);
                  });
                });
              });
            });
          }
        });
      }
      emit("load", {
        side,
        done
      });
    }
    const {
      t
    } = useLocale();
    function renderSide(side, status) {
      var _a, _b, _c, _d, _e;
      if (props.side !== side && props.side !== "both") return;
      const onClick = () => intersecting(side);
      const slotProps = {
        side,
        props: {
          onClick,
          color: props.color
        }
      };
      if (status === "error") return (_a = slots.error) == null ? void 0 : _a.call(slots, slotProps);
      if (status === "empty") return ((_b = slots.empty) == null ? void 0 : _b.call(slots, slotProps)) ?? createVNode("div", null, [t(props.emptyText)]);
      if (props.mode === "manual") {
        if (status === "loading") {
          return ((_c = slots.loading) == null ? void 0 : _c.call(slots, slotProps)) ?? createVNode(VProgressCircular, {
            "indeterminate": true,
            "color": props.color
          }, null);
        }
        return ((_d = slots["load-more"]) == null ? void 0 : _d.call(slots, slotProps)) ?? createVNode(VBtn, {
          "variant": "outlined",
          "color": props.color,
          "onClick": onClick
        }, {
          default: () => [t(props.loadMoreText)]
        });
      }
      return ((_e = slots.loading) == null ? void 0 : _e.call(slots, slotProps)) ?? createVNode(VProgressCircular, {
        "indeterminate": true,
        "color": props.color
      }, null);
    }
    const {
      dimensionStyles
    } = useDimension(props);
    useRender(() => {
      const Tag = props.tag;
      const hasStartIntersect = props.side === "start" || props.side === "both";
      const hasEndIntersect = props.side === "end" || props.side === "both";
      const intersectMode = props.mode === "intersect";
      return createVNode(Tag, {
        "ref": rootEl,
        "class": ["v-infinite-scroll", `v-infinite-scroll--${props.direction}`, {
          "v-infinite-scroll--start": hasStartIntersect,
          "v-infinite-scroll--end": hasEndIntersect
        }],
        "style": dimensionStyles.value
      }, {
        default: () => {
          var _a;
          return [createVNode("div", {
            "class": "v-infinite-scroll__side"
          }, [renderSide("start", startStatus.value)]), rootEl.value && hasStartIntersect && intersectMode && createVNode(VInfiniteScrollIntersect, {
            "key": "start",
            "side": "start",
            "onIntersect": handleIntersect,
            "rootRef": rootEl.value,
            "rootMargin": margin.value
          }, null), (_a = slots.default) == null ? void 0 : _a.call(slots), rootEl.value && hasEndIntersect && intersectMode && createVNode(VInfiniteScrollIntersect, {
            "key": "end",
            "side": "end",
            "onIntersect": handleIntersect,
            "rootRef": rootEl.value,
            "rootMargin": margin.value
          }, null), createVNode("div", {
            "class": "v-infinite-scroll__side"
          }, [renderSide("end", endStatus.value)])];
        }
      });
    });
  }
});

// node_modules/vuetify/lib/labs/VOtpInput/VOtpInput.mjs
import "E:/Project/CyberNomads/cyber-nomads-register-ui/node_modules/vuetify/lib/labs/VOtpInput/VOtpInput.css";
var makeVOtpInputProps = propsFactory({
  autofocus: Boolean,
  divider: String,
  focusAll: Boolean,
  label: {
    type: String,
    default: "$vuetify.input.otp"
  },
  length: {
    type: [Number, String],
    default: 6
  },
  modelValue: {
    type: [Number, String],
    default: void 0
  },
  placeholder: String,
  type: {
    type: String,
    default: "text"
  },
  ...makeDimensionProps(),
  ...makeFocusProps(),
  ...only(makeVFieldProps({
    variant: "outlined"
  }), ["baseColor", "bgColor", "class", "color", "disabled", "error", "loading", "rounded", "style", "theme", "variant"])
}, "VOtpInput");
var VOtpInput = genericComponent()({
  name: "VOtpInput",
  props: makeVOtpInputProps(),
  emits: {
    finish: (val) => true,
    "update:focused": (val) => true,
    "update:modelValue": (val) => true
  },
  setup(props, _ref) {
    let {
      attrs,
      emit,
      slots
    } = _ref;
    const {
      dimensionStyles
    } = useDimension(props);
    const {
      isFocused,
      focus,
      blur
    } = useFocus(props);
    const model = useProxiedModel(props, "modelValue", "", (val) => String(val).split(""), (val) => val.join(""));
    const {
      t
    } = useLocale();
    const fields = computed(() => Array(Number(props.length)).fill(0));
    const focusIndex = ref(-1);
    const contentRef = ref();
    const inputRef = ref([]);
    const current = computed(() => inputRef.value[focusIndex.value]);
    function onInput() {
      const array = model.value.slice();
      const value = current.value.value;
      array[focusIndex.value] = value;
      model.value = array;
      let target = null;
      if (focusIndex.value > model.value.length) {
        target = model.value.length + 1;
      } else if (focusIndex.value + 1 !== Number(props.length)) {
        target = "next";
      } else {
        requestAnimationFrame(() => {
          var _a;
          return (_a = current.value) == null ? void 0 : _a.blur();
        });
      }
      if (target) focusChild(contentRef.value, target);
    }
    function onKeydown(e) {
      const array = model.value.slice();
      const index = focusIndex.value;
      let target = null;
      if (!["ArrowLeft", "ArrowRight", "Backspace", "Delete"].includes(e.key)) return;
      e.preventDefault();
      if (e.key === "ArrowLeft") {
        target = "prev";
      } else if (e.key === "ArrowRight") {
        target = "next";
      } else if (["Backspace", "Delete"].includes(e.key)) {
        array[focusIndex.value] = "";
        model.value = array;
        if (focusIndex.value > 0 && e.key === "Backspace") {
          target = "prev";
        } else {
          requestAnimationFrame(() => {
            inputRef.value[index].select();
          });
        }
      }
      requestAnimationFrame(() => {
        if (target != null) {
          focusChild(contentRef.value, target);
        }
      });
    }
    function onPaste(index, e) {
      var _a, _b;
      e.preventDefault();
      e.stopPropagation();
      model.value = (((_a = e == null ? void 0 : e.clipboardData) == null ? void 0 : _a.getData("Text")) ?? "").split("");
      (_b = inputRef.value) == null ? void 0 : _b[index].blur();
    }
    function reset() {
      model.value = [];
    }
    function onFocus(e, index) {
      focus();
      focusIndex.value = index;
    }
    function onBlur() {
      blur();
      focusIndex.value = -1;
    }
    provideDefaults({
      VField: {
        disabled: computed(() => props.disabled),
        error: computed(() => props.error),
        variant: computed(() => props.variant)
      }
    }, {
      scoped: true
    });
    watch(model, (val) => {
      if (val.length === props.length) emit("finish", val.join(""));
    }, {
      deep: true
    });
    watch(focusIndex, (val) => {
      if (val < 0) return;
      IN_BROWSER && window.requestAnimationFrame(() => {
        inputRef.value[val].select();
      });
    });
    useRender(() => {
      var _a;
      const [rootAttrs, inputAttrs] = filterInputAttrs(attrs);
      return createVNode("div", mergeProps({
        "class": ["v-otp-input", {
          "v-otp-input--divided": !!props.divider
        }, props.class],
        "style": [props.style]
      }, rootAttrs), [createVNode("div", {
        "ref": contentRef,
        "class": "v-otp-input__content",
        "style": [dimensionStyles.value]
      }, [fields.value.map((_, i) => createVNode(Fragment, null, [props.divider && i !== 0 && createVNode("span", {
        "class": "v-otp-input__divider"
      }, [props.divider]), createVNode(VField, {
        "focused": isFocused.value && props.focusAll || focusIndex.value === i,
        "key": i
      }, {
        ...slots,
        default: () => {
          return createVNode("input", {
            "ref": (val) => inputRef.value[i] = val,
            "aria-label": t(props.label, i + 1),
            "autofocus": i === 0 && props.autofocus,
            "autocomplete": "one-time-code",
            "class": ["v-otp-input__field"],
            "inputmode": "text",
            "min": props.type === "number" ? 0 : void 0,
            "maxlength": "1",
            "placeholder": props.placeholder,
            "type": props.type,
            "value": model.value[i],
            "onInput": onInput,
            "onFocus": (e) => onFocus(e, i),
            "onBlur": onBlur,
            "onKeydown": onKeydown,
            "onPaste": (event) => onPaste(i, event)
          }, null);
        }
      })])), createVNode("input", mergeProps({
        "class": "v-otp-input-input",
        "type": "hidden"
      }, inputAttrs, {
        "value": model.value.join("")
      }), null), createVNode(VOverlay, {
        "contained": true,
        "content-class": "v-otp-input__loader",
        "model-value": !!props.loading,
        "persistent": true
      }, {
        default: () => {
          var _a2;
          return [((_a2 = slots.loader) == null ? void 0 : _a2.call(slots)) ?? createVNode(VProgressCircular, {
            "color": typeof props.loading === "boolean" ? void 0 : props.loading,
            "indeterminate": true,
            "size": "24",
            "width": "2"
          }, null)];
        }
      }), (_a = slots.default) == null ? void 0 : _a.call(slots)])]);
    });
    return {
      blur: () => {
        var _a;
        (_a = inputRef.value) == null ? void 0 : _a.some((input) => input.blur());
      },
      focus: () => {
        var _a;
        (_a = inputRef.value) == null ? void 0 : _a[0].focus();
      },
      reset,
      isFocused
    };
  }
});

// node_modules/vuetify/lib/labs/VSkeletonLoader/VSkeletonLoader.mjs
import "E:/Project/CyberNomads/cyber-nomads-register-ui/node_modules/vuetify/lib/labs/VSkeletonLoader/VSkeletonLoader.css";
var rootTypes = {
  actions: "button@2",
  article: "heading, paragraph",
  avatar: "avatar",
  button: "button",
  card: "image, heading",
  "card-avatar": "image, list-item-avatar",
  chip: "chip",
  "date-picker": "list-item, heading, divider, date-picker-options, date-picker-days, actions",
  "date-picker-options": "text, avatar@2",
  "date-picker-days": "avatar@28",
  divider: "divider",
  heading: "heading",
  image: "image",
  "list-item": "text",
  "list-item-avatar": "avatar, text",
  "list-item-two-line": "sentences",
  "list-item-avatar-two-line": "avatar, sentences",
  "list-item-three-line": "paragraph",
  "list-item-avatar-three-line": "avatar, paragraph",
  paragraph: "text@3",
  sentences: "text@2",
  subtitle: "text",
  table: "table-heading, table-thead, table-tbody, table-tfoot",
  "table-heading": "chip, text",
  "table-thead": "heading@6",
  "table-tbody": "table-row-divider@6",
  "table-row-divider": "table-row, divider",
  "table-row": "text@6",
  "table-tfoot": "text@2, avatar@2",
  text: "text"
};
function genBone(type) {
  let children = arguments.length > 1 && arguments[1] !== void 0 ? arguments[1] : [];
  return createVNode("div", {
    "class": ["v-skeleton-loader__bone", `v-skeleton-loader__${type}`]
  }, [children]);
}
function genBones(bone) {
  const [type, length] = bone.split("@");
  return Array.from({
    length
  }).map(() => genStructure(type));
}
function genStructure(type) {
  let children = [];
  if (!type) return children;
  const bone = rootTypes[type];
  if (type === bone) {
  } else if (type.includes(",")) return mapBones(type);
  else if (type.includes("@")) return genBones(type);
  else if (bone.includes(",")) children = mapBones(bone);
  else if (bone.includes("@")) children = genBones(bone);
  else if (bone) children.push(genStructure(bone));
  return [genBone(type, children)];
}
function mapBones(bones) {
  return bones.replace(/\s/g, "").split(",").map(genStructure);
}
var makeVSkeletonLoaderProps = propsFactory({
  boilerplate: Boolean,
  color: String,
  loading: Boolean,
  loadingText: {
    type: String,
    default: "$vuetify.loading"
  },
  type: {
    type: [String, Array],
    default: "image"
  },
  ...makeDimensionProps(),
  ...makeElevationProps(),
  ...makeThemeProps()
}, "VSkeletonLoader");
var VSkeletonLoader = genericComponent()({
  name: "VSkeletonLoader",
  props: makeVSkeletonLoaderProps(),
  setup(props, _ref) {
    let {
      slots
    } = _ref;
    const {
      backgroundColorClasses,
      backgroundColorStyles
    } = useBackgroundColor(toRef(props, "color"));
    const {
      dimensionStyles
    } = useDimension(props);
    const {
      elevationClasses
    } = useElevation(props);
    const {
      themeClasses
    } = provideTheme(props);
    const {
      t
    } = useLocale();
    const items = computed(() => genStructure(wrapInArray(props.type).join(",")));
    useRender(() => {
      var _a;
      const isLoading = !slots.default || props.loading;
      return createVNode("div", {
        "class": ["v-skeleton-loader", {
          "v-skeleton-loader--boilerplate": props.boilerplate
        }, themeClasses.value, backgroundColorClasses.value, elevationClasses.value],
        "style": [backgroundColorStyles.value, isLoading ? dimensionStyles.value : {}],
        "aria-busy": !props.boilerplate ? isLoading : void 0,
        "aria-live": !props.boilerplate ? "polite" : void 0,
        "aria-label": !props.boilerplate ? t(props.loadingText) : void 0,
        "role": !props.boilerplate ? "alert" : void 0
      }, [isLoading ? items.value : (_a = slots.default) == null ? void 0 : _a.call(slots)]);
    });
    return {};
  }
});

// node_modules/vuetify/lib/labs/VStepper/VStepper.mjs
import "E:/Project/CyberNomads/cyber-nomads-register-ui/node_modules/vuetify/lib/labs/VStepper/VStepper.css";

// node_modules/vuetify/lib/labs/VStepper/VStepperActions.mjs
var makeVStepperActionsProps = propsFactory({
  color: String,
  disabled: {
    type: [Boolean, String],
    default: false
  },
  prevText: {
    type: String,
    default: "$vuetify.stepper.prev"
  },
  nextText: {
    type: String,
    default: "$vuetify.stepper.next"
  }
}, "VStepperActions");
var VStepperActions = genericComponent()({
  name: "VStepperActions",
  props: makeVStepperActionsProps(),
  emits: {
    "click:prev": () => true,
    "click:next": () => true
  },
  setup(props, _ref) {
    let {
      emit,
      slots
    } = _ref;
    const {
      t
    } = useLocale();
    function onClickPrev() {
      emit("click:prev");
    }
    function onClickNext() {
      emit("click:next");
    }
    useRender(() => {
      return createVNode("div", {
        "class": "v-stepper-actions"
      }, [createVNode(VBtn, {
        "disabled": ["prev", true].includes(props.disabled),
        "text": t(props.prevText),
        "variant": "text",
        "onClick": onClickPrev
      }, null), createVNode(VBtn, {
        "disabled": ["next", true].includes(props.disabled),
        "color": props.color,
        "text": t(props.nextText),
        "variant": "tonal",
        "onClick": onClickNext
      }, null)]);
    });
    return {};
  }
});

// node_modules/vuetify/lib/labs/VStepper/VStepperHeader.mjs
var VStepperHeader = createSimpleFunctional("v-stepper-header");

// node_modules/vuetify/lib/labs/VStepper/VStepperItem.mjs
import "E:/Project/CyberNomads/cyber-nomads-register-ui/node_modules/vuetify/lib/labs/VStepper/VStepperItem.css";
var makeVStepperItemProps = propsFactory({
  color: String,
  title: String,
  subtitle: String,
  complete: Boolean,
  completeIcon: {
    type: String,
    default: "$complete"
  },
  editable: Boolean,
  editIcon: {
    type: String,
    default: "$edit"
  },
  error: Boolean,
  errorIcon: {
    type: String,
    default: "$error"
  },
  icon: String,
  ripple: {
    type: [Boolean, Object],
    default: true
  },
  rules: {
    type: Array,
    default: () => []
  },
  ...makeGroupItemProps()
}, "VStepperItem");
var VStepperItem = genericComponent()({
  name: "VStepperItem",
  directives: {
    Ripple
  },
  props: makeVStepperItemProps(),
  emits: {
    "group:selected": (val) => true
  },
  setup(props, _ref) {
    let {
      slots
    } = _ref;
    const group = useGroupItem(props, VStepperSymbol, true);
    const step = computed(() => (group == null ? void 0 : group.value.value) ?? props.value);
    const isValid = computed(() => props.rules.every((handler) => handler() === true));
    const canEdit = computed(() => !props.disabled && props.editable);
    const hasError = computed(() => props.error || !isValid.value);
    const hasCompleted = computed(() => props.complete || props.rules.length > 0 && isValid.value);
    const icon = computed(() => {
      if (hasError.value) return props.errorIcon;
      if (hasCompleted.value) return props.completeIcon;
      if (props.editable) return props.editIcon;
      return props.icon;
    });
    const slotProps = computed(() => ({
      canEdit: canEdit.value,
      hasError: hasError.value,
      hasCompleted: hasCompleted.value,
      title: props.title,
      subtitle: props.subtitle,
      step: step.value,
      value: props.value
    }));
    useRender(() => {
      var _a, _b, _c;
      const hasColor = (!group || group.isSelected.value || hasCompleted.value || canEdit.value) && !hasError.value && !props.disabled;
      const hasTitle = !!(props.title || slots.title);
      const hasSubtitle = !!(props.subtitle || slots.subtitle);
      function onClick() {
        group == null ? void 0 : group.toggle();
      }
      return withDirectives(createVNode("button", {
        "class": ["v-stepper-item", {
          "v-stepper-item--complete": hasCompleted.value,
          "v-stepper-item--disabled": props.disabled,
          "v-stepper-item--error": hasError.value
        }, group == null ? void 0 : group.selectedClass.value],
        "disabled": !props.editable,
        "onClick": onClick
      }, [createVNode(VAvatar, {
        "key": "stepper-avatar",
        "class": "v-stepper-item__avatar",
        "color": hasColor ? props.color : void 0,
        "size": 24
      }, {
        default: () => {
          var _a2;
          return [((_a2 = slots.icon) == null ? void 0 : _a2.call(slots, slotProps.value)) ?? (icon.value ? createVNode(VIcon, {
            "icon": icon.value
          }, null) : step.value)];
        }
      }), createVNode("div", {
        "class": "v-stepper-item__content"
      }, [hasTitle && createVNode("div", {
        "key": "title",
        "class": "v-stepper-item__title"
      }, [((_a = slots.title) == null ? void 0 : _a.call(slots, slotProps.value)) ?? props.title]), hasSubtitle && createVNode("div", {
        "key": "subtitle",
        "class": "v-stepper-item__subtitle"
      }, [((_b = slots.subtitle) == null ? void 0 : _b.call(slots, slotProps.value)) ?? props.subtitle]), (_c = slots.default) == null ? void 0 : _c.call(slots, slotProps.value)])]), [[resolveDirective("ripple"), props.ripple && props.editable, null]]);
    });
    return {};
  }
});

// node_modules/vuetify/lib/labs/VStepper/VStepperWindow.mjs
var VStepperSymbol2 = Symbol.for("vuetify:v-stepper");
var makeVStepperWindowProps = propsFactory({
  ...makeVWindowProps({
    mandatory: false
  })
}, "VStepperWindow");
var VStepperWindow = genericComponent()({
  name: "VStepperWindow",
  props: makeVStepperWindowProps(),
  emits: {
    "update:modelValue": (v) => true
  },
  setup(props, _ref) {
    let {
      slots
    } = _ref;
    const group = inject(VStepperSymbol2, null);
    const _model = useProxiedModel(props, "modelValue");
    const model = computed({
      get() {
        var _a;
        if (_model.value != null || !group) return _model.value;
        return (_a = group.items.value.find((item) => group.selected.value.includes(item.id))) == null ? void 0 : _a.value;
      },
      set(val) {
        _model.value = val;
      }
    });
    useRender(() => {
      const [windowProps] = VWindow.filterProps(props);
      return createVNode(VWindow, mergeProps(windowProps, {
        "modelValue": model.value,
        "onUpdate:modelValue": ($event) => model.value = $event,
        "class": "v-stepper-window"
      }), slots);
    });
    return {};
  }
});

// node_modules/vuetify/lib/labs/VStepper/VStepperWindowItem.mjs
var makeVStepperWindowItemProps = propsFactory({
  ...makeVWindowItemProps()
}, "VStepperWindowItem");
var VStepperWindowItem = genericComponent()({
  name: "VStepperWindowItem",
  props: makeVStepperWindowItemProps(),
  setup(props, _ref) {
    let {
      slots
    } = _ref;
    useRender(() => {
      const [windowItemProps] = VWindowItem.filterProps(props);
      return createVNode(VWindowItem, mergeProps(windowItemProps, {
        "class": "v-stepper-window-item"
      }), slots);
    });
    return {};
  }
});

// node_modules/vuetify/lib/labs/VStepper/VStepper.mjs
var VStepperSymbol = Symbol.for("vuetify:v-stepper");
var makeVStepperProps = propsFactory({
  altLabels: Boolean,
  bgColor: String,
  editable: Boolean,
  hideActions: Boolean,
  items: {
    type: Array,
    default: () => []
  },
  itemTitle: {
    type: String,
    default: "title"
  },
  itemValue: {
    type: String,
    default: "value"
  },
  mobile: Boolean,
  nonLinear: Boolean,
  flat: Boolean,
  ...makeGroupProps({
    mandatory: "force",
    selectedClass: "v-stepper-item--selected"
  }),
  ...makeVSheetProps(),
  ...only(makeVStepperActionsProps(), ["prevText", "nextText"])
}, "VStepper");
var VStepper = genericComponent()({
  name: "VStepper",
  props: makeVStepperProps(),
  emits: {
    "update:modelValue": (v) => true
  },
  setup(props, _ref) {
    let {
      slots
    } = _ref;
    const {
      items: _items,
      next,
      prev,
      selected
    } = useGroup(props, VStepperSymbol);
    const {
      color,
      editable,
      prevText,
      nextText
    } = toRefs(props);
    const items = computed(() => props.items.map((item, index) => {
      const title = getPropertyFromItem(item, props.itemTitle, item);
      const value = getPropertyFromItem(item, props.itemValue, index + 1);
      return {
        title,
        value,
        raw: item
      };
    }));
    const activeIndex = computed(() => {
      return _items.value.findIndex((item) => selected.value.includes(item.id));
    });
    const disabled = computed(() => {
      if (props.disabled) return props.disabled;
      if (activeIndex.value === 0) return "prev";
      if (activeIndex.value === _items.value.length - 1) return "next";
      return false;
    });
    provideDefaults({
      VStepperItem: {
        editable,
        prevText,
        nextText
      },
      VStepperActions: {
        color,
        disabled,
        prevText,
        nextText
      }
    });
    useRender(() => {
      const [sheetProps] = VSheet.filterProps(props);
      const hasHeader = !!(slots.header || props.items.length);
      const hasWindow = props.items.length > 0;
      const hasActions = !props.hideActions && !!(hasWindow || slots.actions);
      return createVNode(VSheet, mergeProps(sheetProps, {
        "color": props.bgColor,
        "class": ["v-stepper", {
          "v-stepper--alt-labels": props.altLabels,
          "v-stepper--flat": props.flat,
          "v-stepper--non-linear": props.nonLinear,
          "v-stepper--mobile": props.mobile
        }, props.class],
        "style": props.style
      }), {
        default: () => {
          var _a, _b;
          return [hasHeader && createVNode(VStepperHeader, {
            "key": "stepper-header"
          }, {
            default: () => [items.value.map((item, index) => createVNode(Fragment, null, [!!index && createVNode(VDivider, null, null), createVNode(VStepperItem, item, {
              default: slots[`header-item.${item.value}`] ?? slots.header,
              icon: slots.icon,
              title: slots.title,
              subtitle: slots.subtitle
            })]))]
          }), hasWindow && createVNode(VStepperWindow, {
            "key": "stepper-window"
          }, {
            default: () => [items.value.map((item) => createVNode(VStepperWindowItem, {
              "value": item.value
            }, {
              default: () => {
                var _a2, _b2;
                return ((_a2 = slots[`item.${item.value}`]) == null ? void 0 : _a2.call(slots, item)) ?? ((_b2 = slots.item) == null ? void 0 : _b2.call(slots, item));
              }
            }))]
          }), (_a = slots.default) == null ? void 0 : _a.call(slots, {
            prev,
            next
          }), hasActions && (((_b = slots.actions) == null ? void 0 : _b.call(slots, {
            next,
            prev
          })) ?? createVNode(VStepperActions, {
            "key": "stepper-actions",
            "onClick:prev": prev,
            "onClick:next": next
          }, null))];
        }
      });
    });
    return {
      prev,
      next
    };
  }
});
export {
  VBottomSheet,
  VDataIterator,
  VDataTable,
  VDataTableFooter,
  VDataTableRow,
  VDataTableRows,
  VDataTableServer,
  VDataTableVirtual,
  VDateCard,
  VDatePicker,
  VDatePickerControls,
  VDatePickerHeader,
  VDatePickerMonth,
  VDatePickerYears,
  VInfiniteScroll,
  VOtpInput,
  VPicker,
  VPickerTitle,
  VSkeletonLoader,
  VStepper,
  VStepperActions,
  VStepperHeader,
  VStepperItem,
  VStepperWindow,
  VStepperWindowItem
};
//# sourceMappingURL=vuetify_labs_components.js.map
