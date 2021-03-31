# 2021-03-31

-- 구매회원
create table memb(
    m_no int not null,
    id varchar(50) not null,
    pwd varchar(50) not null,
    name varchar(20) not null,
    nick_name varchar(30) not null,
    email varchar(50) not null,
    tel varchar(30) not null,
    addr varchar(50) not null,
    rdt DATETIME not null default now(),
    state int not null -- 1. 구매회원 2. 판매회원
);


-- 판매회원
create table seller(
    m_no int not null,
    bs_name varchar(30) not null,
    bs_no varchar(30) not null,
    bs_hour varchar(20) not null,
    category int not null
);


-- 구매회원 게시판
create table buyer_board(
    bb_no int not null,
    title varchar(50) not null,
    content text not null,
    writer int not null,
    like_cnt int default 0,
    vw_cnt int default 0,
    cmt_cnt int default 0,
    cdt datetime not null default now()
);


-- 판매회원 게시판
create table seller_board(
    sb_no int not null,
    title varchar(50) not null,
    content text not null,
    writer int not null,
    like_cnt int default 0,
    vw_cnt int default 0,
    cmt_cnt int default 0,
    cdt datetime not null default now()
);


-- 구매회원 게시판 댓글
create table buyer_board_cmt(
    c_no int not null,
    bb_no int not null,
    cmt varchar(255) not null,
    writer int not null,
    cdt datetime not null default now()
);


-- 판매회원 게시판 댓글
create table seller_board_cmt(
    c_no int not null,
    sb_no int not null,
    cmt varchar(255) not null,
    writer int not null,
    cdt datetime not null default now()
);


-- 업종
create table category(
    no int not null,
    korean int default 1,
    western int default 2,
    japanese int default 3,
    chinese int default 4,
    snack int default 5,
    chicken int default 6,
    pizza int default 7,
    dessert int default 8,
    nick_snack int default 9
);


-- 메뉴
create table menu(
    mn_no int not null,
    owner int not null,
    name varchar(30) not null,
    price int default 0 not null,
    menu_explain varchar(100) not null
);


-- 주문
create table order_info(
    o_no int not null,
    m_no int not null,
    odt datetime not null default now(),
    state int not null
);


-- 주문 내용
create table order_content(
    no int not null,
    price int,
    mn_no int not null,
    o_no int not null
);


alter table memb
    add CONSTRAINT memb_PK PRIMARY KEY(m_no),
    add CONSTRAINT memb_UK UNIQUE(tel, nick_name, email);
    
alter table seller
    add CONSTRAINT seller_PK PRIMARY KEY(m_no),
    add CONSTRAINT seller_FK FOREIGN KEY(m_no) REFERENCES memb(m_no),
    add constraint seller_UK UNIQUE(bs_name, bs_no);

alter table buyer_board
    add constraint buyer_board_PK primary key(bb_no),
    add constraint buyer_board_FK foreign key(writer) references memb(m_no),
    MODIFY COLUMN bb_no int auto_increment;


alter table seller_board
    add constraint seller_board_PK primary key(sb_no),
    add constraint seller_board_FK foreign key(writer) references seller(m_no),
    MODIFY COLUMN sb_no int auto_increment;


alter table buyer_board_cmt
    add constraint buyer_board_cmt_PK primary key(c_no),
    add constraint buyer_board_cmt_FK foreign key(bb_no) references buyer_board(bb_no),
    add constraint buyer_board_cmt_FK2 foreign key(writer) references memb(m_no),
    MODIFY COLUMN c_no int auto_increment;

alter table seller_board_cmt
    add constraint seller_board_cmt_PK primary key(c_no),
    add constraint seller_board_cmt_FK foreign key(sb_no) references seller_board(sb_no),
    add constraint seller_board_cmt_FK2 foreign key(writer) references seller(m_no),
    MODIFY COLUMN c_no int auto_increment;

alter table category
    add constraint category_pk primary key (no),
    add constraint category_fk foreign key(no) references seller(m_no),
    modify column no int auto_increment;

alter table menu
    add constraint menu_PK primary key(mn_no),
    add constraint menu_FK foreign key(owner) references seller(m_no),
    modify column mn_no int auto_increment; 

alter table order_info
    add constraint order_info_PK primary key(o_no),
    add constraint order_info_FK foreign key(m_no) references memb(m_no),
    modify column o_no int auto_increment;

alter table order_content
    add constraint order_content_PK primary key(no),
    add constraint order_content_FK foreign key(o_no) references order_info(m_no),
    add constraint order_content_FK2 foreign key(mn_no) references menu(owner),
    modify column no int auto_increment;