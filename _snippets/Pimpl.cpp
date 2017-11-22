{#- .name: C++ pointer-to-implementation pattern -#}
{#- .description: Follows best practices, I hope -#}
{#- .default(class_name): MyClass -#}
////////// BEGIN {{class_name}}.h //////////
#pragma once

class {{class_name}} final
{
public:
    {{class_name}}(const {{class_name}}&) = delete;
    {{class_name}}& operator=(const {{class_name}}&) = delete;

public:
    {{class_name}}();

    ~{{class_name}}();

    const std::string& message() const;

private:
    std::unique_ptr<class {{class_name}}Impl> m_impl;
};
////////// BEGIN {{class_name}}.cpp //////////
#include "{{class_name}}.h"

class {{class_name}}Impl final
{
public:
    {{class_name}}Impl(const {{class_name}}Impl&) = delete;
    {{class_name}}Impl& operator=(const {{class_name}}Impl&) = delete;

public:
    {{class_name}}Impl() : m_message("Hello world") { }

    ~{{class_name}}Impl() = default;

    const string& message() const { return m_message; }

private:
    const string m_message;
};

{{class_name}}::{{class_name}}()
    : m_impl(make_unique<{{class_name}}Impl>())
{
}

{{class_name}}::~{{class_name}}()
{
}

const string& {{class_name}}::message() const
{
    return m_impl->message();
}
