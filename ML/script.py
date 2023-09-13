
import os
from tflite_support import metadata as _metadata
from tflite_support import metadata_schema_py_generated as _metadata_fb
model_file = '/home/saraiva/Desktop/University/SI/SA/SA/ML/tfconvertedmodel.tflite'
displayer = _metadata.MetadataDisplayer.with_model_file(model_file)
export_json_file = os.path.join(os.path.splitext(model_file)[0] + ".json")
json_file = displayer.get_metadata_json()
with open(export_json_file, "w") as f:
    f.write(json_file)