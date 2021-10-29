$(function () {
    $(".address").hover(function () {
        $(this).addClass("address-hover");
    }, function () {
        $(this).removeClass("address-hover");
    });
})

$(function () {
    $(".addr-search .name").click(function () {
        $(this).toggleClass("selected").siblings().removeClass("selected");
    });
    $(".payType li").click(function () {
        $(this).toggleClass("selected").siblings().removeClass("selected");
    });
})
