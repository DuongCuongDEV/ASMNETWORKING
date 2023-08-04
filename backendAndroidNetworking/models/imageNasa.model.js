const db = require("./connectDatabase");

const ImageNasaSchema = new db.mongoose.Schema(
  {
    copyright: { type: String },
    date: { type: String },
    explanation: { type: String },
    hdurl: { type: String },
    media_type: { type: String },
    service_version: { type: String },
    title: { type: String },
    url: { type: String },
  },
  {
    timestamps: true,
  }
);

let imageNasa = db.mongoose.model("imageNasa", ImageNasaSchema);
module.exports = {
  imageNasa,
};
