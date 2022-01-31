# This script parsers the "Terrascript Functions" wiki page html and generates a json of all functions and their
# argument types
import json
import os.path
import sys
from xml.etree.ElementTree import Element, SubElement, tostring, indent

from bs4 import BeautifulSoup, NavigableString


def collect_section(element):
    section_els = []
    element = element.next_sibling
    while element is not None and element.name != 'h2':
        section_els.append(element)
        element = element.next_sibling
    return section_els


def text_of(element):
    if isinstance(element, NavigableString):
        return str(element)
    else:
        return element.text


def next_sibling_skip(element):
    i = 1
    element = element.next_sibling
    while element is not None and text_of(element).isspace():
        element = element.next_sibling
        i += 1
    return element, i


def trim_els(els):
    head = 0
    for head, x in enumerate(els):
        text = text_of(x)
        if text != "" and not text.isspace():
            break
    delta_tail = 0
    for delta_tail, x in enumerate(reversed(els)):
        text = text_of(x)
        if text != "" and not text.isspace():
            break
    return els[head:len(els) - delta_tail]


def extract_args(section_els):
    result = []
    var_args = None
    for i, el in enumerate(section_els):
        if text_of(el).strip().startswith('Arguments:'):
            table, delta_i = next_sibling_skip(el)
            last = i + delta_i
            if table is not None and table.name == 'table':
                last += 1
                for row in table.find_all('tr'):
                    row = row.find_all('td')
                    if not row:
                        continue
                    [name, ty, doc] = row
                    ty_s = text_of(ty)
                    if ty_s.endswith("..."):
                        ty_s = ty_s.removesuffix("...")
                        var_args = (text_of(name).strip(), which_type(ty_s), doc)
                    result.append((text_of(name).strip(), which_type(ty_s), doc))
            del section_els[i:last]
    return result, var_args


def which_type(type_s: str):
    type_s = type_s.lower()
    types = {
        'number': 'num',
        'string': 'str',
        'boolean': 'bool',
    }
    actual = list(types.values()) + ['void']
    return types.get(type_s, type_s if type_s in actual else None)


def extract_return(section_els):
    for i, x in enumerate(section_els):
        if text_of(x).strip().startswith('Returns:'):
            children = list(x.children)
            ty = children[1]
            for child in children[0:2]:
                child.extract()
            return which_type(text_of(ty)), trim_els(section_els[i::])


def extract_functions(html: BeautifulSoup):
    body = html.select_one('div[class="markdown-body"]')
    funcs = body.find_all('h2')
    for func in funcs:
        name = func.text.strip()
        doc_and_more = collect_section(func)
        args = extract_args(doc_and_more)
        ret = extract_return(doc_and_more)
        yield name, args, ret, trim_els(doc_and_more)


def format_arg(arg):
    name, ty, _ = arg
    return f"{name}: {ty}"


def main_print_debug(funcs_wiki: str):
    soup = BeautifulSoup(funcs_wiki, 'html.parser')
    for name, (args, var_args), (ret, ret_doc), doc in extract_functions(soup):
        if var_args is not None:
            name, ty, doc = var_args
            args.append((name, ty + "...", doc))
        print(f"{name}({', '.join(map(format_arg, args))}) -> {ret}:")
        print(doc)


def main_print_xml(funcs_wiki: str):
    soup = BeautifulSoup(funcs_wiki, 'html.parser')
    root = Element('terrascript-funcs')
    for name, (args, var_args), (ret, ret_doc), doc in extract_functions(soup):
        func = SubElement(root, "func", {"name": name, "returnType": ret})
        for arg_name, arg_ty, arg_doc in args:
            SubElement(func, "arg", {"name": arg_name, "type": arg_ty})
        if var_args:
            arg_name, arg_ty, arg_doc = var_args
            SubElement(func, "vararg", {"name": arg_name, "type": arg_ty})
    indent(root, space="\t")
    sys.stdout.buffer.write(tostring(root, encoding="utf-8", method="xml"))


def ty_to_enum(ty: str):
    return ty.upper()


def collect_doc(doc):
    return "".join(map(text_of, doc))


def arg_is_optional(doc):
    return "optional" in collect_doc(doc)


def main_print_json(funcs_wiki: str):
    soup = BeautifulSoup(funcs_wiki, 'html.parser')
    root = []
    for name, (args, var_args), (ret, ret_doc), doc in extract_functions(soup):
        my_args = []
        for arg_name, arg_ty, arg_doc in args:
            this_arg = {"name": arg_name, "type": ty_to_enum(arg_ty)}
            if arg_is_optional(arg_doc):
                this_arg["isOptional"] = True
            my_args.append(this_arg)

        func = {"name": name, "returnType": ty_to_enum(ret), "args": my_args}
        if var_args:
            arg_name, arg_ty, arg_doc = var_args
            func["varArg"] = {"name": arg_name, "type": ty_to_enum(arg_ty)}
        root.append(func)
    print(json.dumps({"funcs": root}))


if __name__ == '__main__':
    file_name = os.path.join(os.path.dirname(__file__), "Terrascript-Functions.html")
    with open(file_name, 'r') as f:
        # funcs_wiki = requests.get('https://github.com/PolyhedralDev/Terra/wiki/TerraScript-Functions')
        main_print_json(f.read())
