var express = require('express');
var router = express.Router();
const controller = require('../controllers/imageNasa.controller');

router.get('/',controller.list);
router.post('/add',controller.add);
router.put('/update/:id',controller.update);
router.delete('/delete/:id',controller.delete);

module.exports = router;
