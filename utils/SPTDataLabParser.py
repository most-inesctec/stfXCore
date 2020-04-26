import argparse
import os
import re
import json


def parse_args():
    """Parse the command line arguments"""
    ap = argparse.ArgumentParser(
        description='Script to parse SPTDataLab output file to a json file')

    ap.add_argument('-i', '--input_file', type=str,
                    required=True, help='The input file containing the SPTDataLab output')
    ap.add_argument('-o', '--output_dir', type=str,
                    default='./', help='Output directory for the datasets. Default is current directory.')
    ap.add_argument('-of', '--output_file', type=str,
                    default='out', help='Output file name')

    return ap.parse_args()


def create_dir(dir_name: str):
    """Creates a directory wth the given name if it does not exist"""
    if not os.path.exists(dir_name):
        os.makedirs(dir_name)


def save(dataset: dict, out_file: str, out_dir: str):
    """Save the content to the given json file in the given dir"""
    with open("%s/%s.json" % (out_dir, out_file), "w+") as fd:
        fd.write(json.dumps(dataset))


def process_line(line: str) -> list:
    """Process a line and return the respective list"""
    out = []

    for match in re.findall(r"(-?(\d|\.)+ -?(\d|\.)+)", line):
        out.append(
            list(map(lambda x: float(x), match[0].split(' '))))

    return out


def parse(in_file: int) -> dict:
    """Parse the output into a dataset object"""
    with open("%s" % in_file, "r") as fd:
        dataset = []

        lines = fd.readlines()

        # Processing each line
        for line in lines:
            out = process_line(line)
            dataset.append(out)

    return {'dataset': dataset}


def main():
    """Main function"""
    args = parse_args()

    create_dir(args.output_dir)
    dataset = parse(args.input_file)
    save(dataset,
         args.output_file,
         args.output_dir,)


if __name__ == '__main__':
    main()
