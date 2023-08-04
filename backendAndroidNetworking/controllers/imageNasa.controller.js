const model = require("../models/imageNasa.model");

exports.list = async (req, res, next) => {
  try {
    var image = await model.imageNasa.find();
    if (image) {
      return res
        .status(200)
        .json({ status: 200, data: image, message: "get data successfully" });
    } else {
      return res.status(401).json({ status: 401, message: "not found" });
    }
  } catch (error) {
    return res.status(500).json({ status: 500, message: error.message });
  }
};
exports.add = async (req, res, next) => {
  try {
    const imageNasa = new model.imageNasa(req.body);
    console.log(imageNasa);
    let newImage = await imageNasa.save();
    console.log(newImage);
    return res.status(201).json({
      status: 201,
      data: newImage,
      message: "Image added successfully",
    });
  } catch (error) {
    return res.status(500).json({
      status: 500,
      message: error.message,
    });
  }
};
exports.update = async (req, res, next) => {
  const imageId = req.params.id;
  const updateData = req.body;

  console.log("imageId" + imageId);
  try {
    const updatedImage = await model.imageNasa.findByIdAndUpdate(
      imageId,
      updateData
    );

    if (updatedImage) {
      return res.status(200).json({
        status: 200,
        message: "Image updated successfully",
      });
    } else {
      return res.status(404).json({
        status: 404,
        message: "Image not found",
      });
    }
  } catch (error) {
    return res.status(500).json({
      status: 500,
      message: error.message,
    });
  }
};
exports.delete = async (req, res, next) => {
  const imageId = req.params.id;
  try {
    await model.imageNasa.findByIdAndDelete(imageId);
    return res.status(200).json({
      status: 200,
      message: "Image deleted successfully",
    });
  } catch (error) {
    return res.status(500).json({
      status: 500,
      message: error.message,
    });
  }
};
