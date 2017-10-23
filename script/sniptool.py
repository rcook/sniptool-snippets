#!/usr/bin/env python
##################################################
# Copyright (C) 2017, All rights reserved.
##################################################

from __future__ import print_function
import argparse
import jinja2
import jinja2.meta
import os

from pyprelude.file_system import make_path

def _has_prefix(s, prefix):
    return s[len(prefix) : ] if s.startswith(prefix) else None

def _has_suffix(s, suffix):
    return s[: -len(suffix)] if s.endswith(suffix) else None

def _read_source(path):
    with open(path, "rt") as f:
        return f.read()

def _read_metadata(source):
    metadata = {}
    for line in source.splitlines():
        line = _has_prefix(line, "{#")
        if not line: continue
        line = _has_suffix(line, "#}")
        if not line: continue
        line = line.strip(" -")
        line = _has_prefix(line, ".")
        if not line: continue
        index = line.find(":")
        if not index: continue

        key = line[0 : index].strip()
        value = line[index + 1:].strip()
        metadata[key] = value

    return metadata

def _prompt(env, source):
    template = env.parse(source)
    values = {}
    for key in sorted(jinja2.meta.find_undeclared_variables(template)):
        value = raw_input("Enter value for \"{}\": ".format(key))
        values[key] = value

    return values

def _main(args):
    env = jinja2.Environment(undefined=jinja2.StrictUndefined)
    for p in os.listdir(args.template_dir):
        path = make_path(args.template_dir, p)
        source = _read_source(path)
        metadata = _read_metadata(source)
        print("Path: {}".format(path))
        print("Name: {}".format(metadata.get("name", "(unnamed)")))
        print("Description: {}".format(metadata.get("description", "(no description)")))
        values = _prompt(env, source)
        print(env.from_string(source, values).render())

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--template-dir", "-t", default=make_path(__name__, "..", "_data"))
    args = parser.parse_args()
    _main(args)