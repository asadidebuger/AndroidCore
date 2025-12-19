function softScrollById(id) {
    $('html,body').animate({scrollTop: $(id).offset().top}, 'slow');
}
function softScrollByPosition(position) {
    $('html,body').animate({scrollTop: '+=' + position}, 'slow');
}
function hardScrollByPosition(position) {
    $('html,body').animate({scrollTop: '+=' + position}, 0);
}