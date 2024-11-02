// 通用的校验器对象类型
export type Validators<T> = {
  [K in keyof T]: (value: T[K]) => string | null;
};

// 通用的表单校验函数
export function validateForm<T>(
  fieldsToValidate: (keyof T)[], // 要校验的字段
  form: T,                       // 表单数据对象
  validators: Validators<T>      // 校验器对象
): Partial<Record<keyof T, string>> {
  const errors: Partial<Record<keyof T, string>> = {};

  fieldsToValidate.forEach(field => {
    const validator = validators[field];
    const error = validator(form[field]);
    if (error) errors[field] = error;
  });

  return errors;
}


export function validateAndReturn<T>( fieldsToValidate: (keyof T)[], // 要校验的字段
                            form: T,                       // 表单数据对象
                            validators: Validators<T> ): string | null {

  const errors = validateForm(fieldsToValidate,form,validators)
  // 获取所有包含错误信息的字段
  const errorEntries = Object.entries(errors) as [keyof T, string][];

  // 查找第一个非空的错误信息
  const firstError = errorEntries.find(([_, error]) => error !== undefined && error !== null);

  // 如果找到，则返回格式化的错误信息，否则返回 null
  return firstError ? `${firstError[1]}` : null;
}
